package com.github.fauu.monmonde.sim.util

import java.util.concurrent.ThreadLocalRandom

object Random {
  operator fun invoke() = ThreadLocalRandom.current()!!
}
