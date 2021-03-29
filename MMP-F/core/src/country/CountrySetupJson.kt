package com.github.fauu.monmonde.sim.country

import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class CountrySetupJson(
  val code: String,
  val name: String,
  val unavailableUntil: ZonedDateTime?
)