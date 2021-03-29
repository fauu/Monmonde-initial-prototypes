package com.github.fauu.monmonde.sim.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter {
  @FromJson
  fun from(s: String) = LocalDate.parse(s, DateTimeFormatter.ISO_DATE)

  @ToJson
  fun to(localDate: LocalDate) = localDate.format(DateTimeFormatter.ISO_DATE)
}