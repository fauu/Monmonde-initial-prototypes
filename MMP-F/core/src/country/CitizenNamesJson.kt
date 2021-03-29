package com.github.fauu.monmonde.sim.country

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CitizenNamesJson(
  val nameCountPerSex: Int,
  val names: List<String>
)