package com.github.fauu.monmonde.sim

import com.github.fauu.monmonde.sim.actor.PersonalInformation
import com.github.fauu.monmonde.sim.actor.Sex
import com.github.fauu.monmonde.sim.actor.Trainer
import com.github.fauu.monmonde.sim.battle.Battle
import com.github.fauu.monmonde.sim.country.Country
import com.github.fauu.monmonde.sim.ranking.RatingPeriod
import com.github.fauu.monmonde.sim.util.*
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import java.lang.Math.pow
import java.time.*
import kotlin.math.roundToInt

typealias Event = () -> Unit

private const val TAU = 1.0

class Simulation(private val countries: List<Country>, var dateTime: ZonedDateTime) {
  private val yearlySchedule = YearlySchedule()

  private var countryLeagueAdmissionQueue =
    countries.sortedByDescending { it.expansionFactor }.toMutableList()

  private val trainers = mutableListOf<Trainer>()

  private var ratingPeriod = RatingPeriod(TAU)

  private val events = mapOf(
    "LeagueCountryAdmissionDay" to event@{
      getCountriesToAdmitForYearCount(dateTime.year, countryLeagueAdmissionQueue)
        .let { 1..it }
        .forEach {
          lateinit var countryToAdmit: Country
          do {
            if (countryLeagueAdmissionQueue.isEmpty()) return@event
            countryToAdmit = countryLeagueAdmissionQueue[0]
            countryLeagueAdmissionQueue.removeAt(0)
            val canAdmit = !(countryToAdmit.unavailableUntilDate?.isAfter(dateTime) ?: false)
          } while (!canAdmit)
          countryToAdmit.leagueAdmissionDate = dateTime.plusDays(0) // TODO: Fix hack?
        }
    },
    "LeagueTrainerAdmissionDay" to {
      countries.filter { it.isLeagueMember }
        .forEach {
          val softLimit = it.newTrainerLimit * 0.8
          val base = softLimit / 128 // roughly 32 years * 4 admission days
          val modifierHalfRange = (base / 3).roundToInt()
          val modifier = Random().nextInt(-modifierHalfRange, modifierHalfRange + 1)
          val toAdmitCount = (base + modifier).roundToInt()
          admitTrainersFromCountry(toAdmitCount, it, dateTime)
        }
    }
  )

  init {
    shuffleCountryLeagueAdmissionQueue()
    scheduleEventsForTheYear()
  }

  private fun admitTrainersFromCountry(count: Int, country: Country, dateTime: ZonedDateTime) {
    val newTrainers = mutableListOf<Trainer>()
    count times {
      val sex = if (Random().nextBoolean()) Sex.MALE else Sex.FEMALE
      val name = country.citizenNamesQueue.pop(sex)

      val currentAge = Period.of(
        18 + Random().nextInt(0, 6), // TODO: Gaussian
        0,
        Random().nextInt(0, 367)

      )
      val birthDate = dateTime.minus(currentAge)
      val personalInformation = PersonalInformation(name, sex, country, birthDate)
      Trainer(personalInformation, dateTime.plusDays(0)) // TODO: Fix date hack?
        .also { newTrainers.add(it) }
    }
    trainers.addAll(newTrainers)
    ratingPeriod.registerEntrants(newTrainers)
  }

  private fun shuffleCountryLeagueAdmissionQueue() {
    countryLeagueAdmissionQueue.run {
      for (i in 0 until size) {
        val swapDistance =
          clamp(Random().nextGaussian(0.1, 20.0).toInt(), 0, size - i - 1)
        swap(i, i + swapDistance)
      }
    }
  }

  fun advanceTo(futureDateTime: ZonedDateTime): Result<String, String> {
    if (!futureDateTime.isAfter(dateTime)) return Err("Can't go back in time")
    return Duration.between(dateTime, futureDateTime).toMinutes().let {
      advanceMinutes(it).map { "Advanced to ${getFormattedDateTime(dateTime)}" }
    }
  }

  private fun advanceMinutes(count: Long): Result<Unit, String> {
    if (count <= 0) return Err("Can't advance non-positive number of minutes")
    for (currentMinute in 0 until count) {
      advanceMinute().also { if (it is Error) return it }
    }
    return Ok(Unit)
  }

  private fun advanceMinute(): Result<Unit, String> {
    if (dateTime.isStartOfDay()) {
      if (dateTime.dayOfYear == 1) runYearlyRouties()
      if (dateTime.dayOfWeek == DayOfWeek.MONDAY) runWeeklyRoutines()
      runDailyRoutines()
    }
    dateTime = dateTime.plusMinutes(1)
    return Ok(Unit)
  }

  private fun runDailyRoutines() {
    yearlySchedule.runEventsForDayOf(dateTime)
    updateTrainersBattlePower()
    performBattles()
  }

  private fun runWeeklyRoutines() {
    ratePeriod()
  }

  private fun runYearlyRouties() {
    scheduleEventsForTheYear()
  }

  private fun ratePeriod() {
    ratingPeriod.rate()
    ratingPeriod = RatingPeriod(TAU)
//    val x = trainers.toList().sortedByDescending { it.rating.value }
    ratingPeriod.registerEntrants(trainers)
  }

  private fun updateTrainersBattlePower() {
    trainers.forEach {
      it.battlePower += Random().nextInt(-2, 3)
    }
  }

  private fun performBattles() {
    trainers.forEachIndexed { index, trainer ->
      val battleCount = Random().nextLong(0, 3)
      val opponentIndices = Random().ints(battleCount, 0, trainers.size)
        .filter { it != index }.toArray()
      opponentIndices.forEach {
        val opponent = trainers[it]
        val battle = Battle(Pair(trainer, opponent))
        battle.perform().run {
          winner.battlePower++
          winner.wins++
          loser.battlePower--
          loser.losses++
          ratingPeriod.registerResult(winner, loser)
        }
      }
    }
  }

  private fun scheduleEventsForTheYear() {
    yearlySchedule.run {
      clear()
      addEvent(
        LocalDate.of(dateTime.year, 11, 21),
        events["LeagueCountryAdmissionDay"]!!
      )
      addEvent(
        LocalDate.of(dateTime.year, 1, 1),
        events["LeagueTrainerAdmissionDay"]!!
      )
      addEvent(
        LocalDate.of(dateTime.year, 3, 1),
        events["LeagueTrainerAdmissionDay"]!!
      )
      addEvent(
        LocalDate.of(dateTime.year, 6, 1),
        events["LeagueTrainerAdmissionDay"]!!
      )
      addEvent(
        LocalDate.of(dateTime.year, 9, 1),
        events["LeagueTrainerAdmissionDay"]!!
      )
      addEvent(
        LocalDate.of(dateTime.year, 12, 1),
        events["LeagueTrainerAdmissionDay"]!!
      )
    }
  }

  private fun getCountriesToAdmitForYearCount(year: Int, admissionQueue: List<Country>): Int {
    val simulationStartYear = 1999 // TODO: DRY
    val firstYearAdmittedCountBase = 30
    val baseRandomModifier = Random().nextInt(-2, 3)
    val fallOffExponent = 2.0
    val admittedCountBias = 1

    val fallOffFactor = pow(
      (year - simulationStartYear + 1).toDouble(),
      fallOffExponent
    )
    val countriesToAdmitCountBase =
      ((firstYearAdmittedCountBase / fallOffFactor) + admittedCountBias).roundToInt()
    return clamp(countriesToAdmitCountBase + baseRandomModifier, 0, admissionQueue.size)
  }
}

