package com.github.fauu.monmonde.sim

import com.github.fauu.monmonde.sim.actor.Sex
import com.github.fauu.monmonde.sim.country.CitizenNames
import com.github.fauu.monmonde.sim.country.CitizenNamesJson
import com.github.fauu.monmonde.sim.country.Country
import com.github.fauu.monmonde.sim.country.CountrySetupJson
import com.github.fauu.monmonde.sim.json.ZonedDateTimeAdapter
import com.github.michaelbull.result.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val COUNTRY_SETUP_FILE_PATH = "country_setup.json"
private const val CITIZEN_NAMES_FILE_PATH = "citizen_names.json"
private const val SIMULATION_START_DATETIME = "1999-11-21T00:00:00+00:00"

class SimulationManager {
  lateinit var simulation: Simulation

  fun initSimulation(): Result<String, String> {
    val moshi = Moshi.Builder()
      .add(ZonedDateTimeAdapter())
      .build()

    val startDateTime =
      ZonedDateTime.parse(SIMULATION_START_DATETIME, DateTimeFormatter.ISO_ZONED_DATE_TIME)

    val countriesSetupJson = loadCountriesSetupJson(moshi).getOrElse { return Err(it) }
    val citizenNames = loadCitizenNames(moshi).getOrElse { return Err(it) }
    return createCountries(countriesSetupJson, citizenNames)
      .andThen { countries ->
        simulation = Simulation(countries, startDateTime)
        Ok("The simulation has been initialized")
      }
  }

  private fun loadCitizenNames(moshi: Moshi): Result<Map<String, CitizenNamesJson>, String> {
    val type = Types.newParameterizedType(
      Map::class.java,
      String::class.java,
      CitizenNamesJson::class.java
    )
    val adapter = moshi.adapter<Map<String, CitizenNamesJson>>(type)
    return Result.of { adapter.fromJson(File(CITIZEN_NAMES_FILE_PATH).readText()).orEmpty() }
      .mapError { "Couldn't read citizen names file $CITIZEN_NAMES_FILE_PATH" }
  }

  private fun loadCountriesSetupJson(moshi: Moshi): Result<List<CountrySetupJson>, String> {
    val type = Types.newParameterizedType(List::class.java, CountrySetupJson::class.java)
    val adapter = moshi.adapter<List<CountrySetupJson>>(type)
    return Result.of { adapter.fromJson(File(COUNTRY_SETUP_FILE_PATH).readText()).orEmpty() }
      .mapError { "Couldn't read country setup file $COUNTRY_SETUP_FILE_PATH" }
  }

  private fun createCountries(
    countriesSetupJson: List<CountrySetupJson>,
    citizenNamesJson: Map<String, CitizenNamesJson>
  ): Result<List<Country>, String> {
//    val maxCitizenNameCount =
//        citizenNamesJson.values.fold(0, { acc, e -> max(2 * e.nameCountPerSex, acc) })
    return countriesSetupJson.map { setupJson ->
      getCountryCitizenNamesFromParsedJson(citizenNamesJson[setupJson.code])
        .map { Country(setupJson, it) }
    }.combine()
  }

  private fun getCountryCitizenNamesFromParsedJson(json: CitizenNamesJson?)
      : Result<CitizenNames, String> {
    if (json == null) return Ok(CitizenNames())

    val nameCount = json.names.size
    if (nameCount != 2 * json.nameCountPerSex) {
      return Err("Citizen name count doesn't match declared number")
    } else if (nameCount == 0) {
      return Ok(CitizenNames())
    }

    val chunkedNames = json.names.chunked(json.nameCountPerSex).also {
      if (it[0].size != it[1].size) {
        return Err("Expected equal count of male and female names")
      }
    }

    return CitizenNames().apply {
      this[Sex.MALE] = chunkedNames[0].shuffled()
      this[Sex.FEMALE] = chunkedNames[1].shuffled()
    }.let { Ok(it) }
  }
}