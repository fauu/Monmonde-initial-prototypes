package com.github.fauu.monmonde.sim

import java.time.LocalDate
import java.time.ZonedDateTime

private const val MAX_DAYS_IN_YEAR = 366

class YearlySchedule {
  private val dayToEvents = Array(MAX_DAYS_IN_YEAR, { mutableListOf<Event>() })

  fun clear() = dayToEvents.forEach { it.clear() }

  fun addEvent(localDate: LocalDate, event: Event) = dayToEvents[localDate.dayOfYear].add(event)

  fun runEventsForDayOf(dateTime: ZonedDateTime) {
    dayToEvents[dateTime.dayOfYear - 1].forEach { it() }
  }
}