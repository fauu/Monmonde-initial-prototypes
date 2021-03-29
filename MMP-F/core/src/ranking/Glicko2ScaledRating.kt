package com.github.fauu.monmonde.sim.ranking

const val CONVERSION_COEFFICIENT = 173.7178f

data class Glicko2ScaledRating(
  var value: Double,
  var deviation: Double,
  var volatility: Double
) {
  constructor(rating: Rating) : this(
    (rating.value - 1500) / CONVERSION_COEFFICIENT,
    rating.deviation / CONVERSION_COEFFICIENT,
    rating.volatility
  )
}
