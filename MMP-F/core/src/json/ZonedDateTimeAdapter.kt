package com.github.fauu.monmonde.sim.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeAdapter {
  @FromJson
  fun from(s: String): ZonedDateTime {
    return ZonedDateTime.parse("${s}T00:00Z", DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }

  @ToJson
  fun to(zonedDateTime: ZonedDateTime) = zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
}