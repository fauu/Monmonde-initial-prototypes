package com.github.fauu.monmonde.sim.actor

import com.github.fauu.monmonde.sim.country.Country
import java.time.ZonedDateTime

data class PersonalInformation(
  val name: PersonName,
  val sex: Sex,
  val country: Country,
  val birthDate: ZonedDateTime
)
