package com.github.fauu.monmonde.sim.util

import java.util.concurrent.ThreadLocalRandom

fun ThreadLocalRandom.nextGaussian(mu: Double, sigma: Double): Double {
  require(sigma > 0) { "Sigma must be positive" }
  return (this.nextGaussian() * mu) + sigma
}
