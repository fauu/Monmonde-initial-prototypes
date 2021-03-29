package com.github.fauu.monmonde.sim.util

import java.time.ZonedDateTime

fun ZonedDateTime.isStartOfDay() = this.hour == 0 && this.minute == 0
