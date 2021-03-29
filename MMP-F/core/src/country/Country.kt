package com.github.fauu.monmonde.sim.country

import java.time.ZonedDateTime

data class Country(
  val name: String,
  val code: String,
  val unavailableUntilDate: ZonedDateTime?, // TODO: Zoned?
  val citizenNamesQueue: CitizenNames,
  var leagueAdmissionDate: ZonedDateTime? = null
) {
  val isLeagueMember: Boolean
    get() = leagueAdmissionDate != null

  val expansionFactor: Int
    get() = citizenNamesQueue.size

  val newTrainerLimit: Int
    get() = citizenNamesQueue.size

  companion object {
    operator fun invoke(setupJson: CountrySetupJson, citizenNames: CitizenNames): Country {
      return Country(setupJson.name, setupJson.code, setupJson.unavailableUntil, citizenNames)
    }
  }
}