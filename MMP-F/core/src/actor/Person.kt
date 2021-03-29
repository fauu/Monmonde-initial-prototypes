package com.github.fauu.monmonde.sim.actor

import com.github.fauu.monmonde.sim.country.Country
import java.time.ZonedDateTime

interface Person {
  val firstName: String
  val lastName: String
  val fullName: String
  val country: Country
  val birthDate: ZonedDateTime
}