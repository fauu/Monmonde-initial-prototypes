package com.github.fauu.monmonde.sim.ranking

data class Rating(
  var value: Double = 1500.0,
  var deviation: Double = 350.0,
  var volatility: Double = 0.06
)