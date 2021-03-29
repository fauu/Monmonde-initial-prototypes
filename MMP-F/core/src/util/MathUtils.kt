package com.github.fauu.monmonde.sim.util

import kotlin.math.max
import kotlin.math.min

fun clamp(value: Int, min: Int, max: Int) = max(min, min(max, value))
