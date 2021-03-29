package com.github.fauu.monmonde.sim.util

import com.github.michaelbull.result.Result
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getFormattedDateTime(date: ZonedDateTime) = date.format(DateTimeFormatter.RFC_1123_DATE_TIME)!!

fun getFormattedCurrentDateTime() = getFormattedDateTime(ZonedDateTime.now())

fun parseDate(dateString: String): Result<ZonedDateTime, Exception> {
  return Result.of {
    ZonedDateTime.parse("${dateString}T00:00:00Z", DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }
}
