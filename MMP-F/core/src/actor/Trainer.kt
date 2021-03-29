package com.github.fauu.monmonde.sim.actor

import com.github.fauu.monmonde.sim.ranking.RankingEntrant
import com.github.fauu.monmonde.sim.ranking.Rating
import com.github.fauu.monmonde.sim.util.Random
import com.github.fauu.monmonde.sim.util.clamp
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class Trainer(
  private val personalInformation: PersonalInformation,
  val leagueAdmissionDate: ZonedDateTime
) : Person, RankingEntrant {
  override val firstName get() = personalInformation.name.first

  override val lastName get() = personalInformation.name.last

  override val fullName get() = personalInformation.name.toString()

  override val country get() = personalInformation.country

  override val birthDate get() = personalInformation.birthDate

  override val rating = Rating()

  var battlePower: Int = Random().nextInt(50, 251)
    set(value) {
      clamp(value, 0, 1000)
    }

  var wins = 0
  var losses = 0

  override fun toString(): String {
    return "$fullName [${rating.value.roundToInt()}±${2 * rating.deviation.roundToInt()}], ${country.name}, $birthDate, $battlePower, $leagueAdmissionDate, (W: $wins, L: $losses)"
  }
}