package com.github.fauu.monmonde.shared.types

enum class MonMove(private val displayName: String,
                   val power: Int,
                   val speed: Int) {
  TACKLE("Tackle", 50, 100),
  QUICK_ATTACK("Quick Attack", 10, 300),
  GUST("Gust", 40, 60),
  VINE_WHIP("Vine Whip", 45, 90);

  val executionTime: Float
    get() = 300f / speed

  override fun toString(): String = displayName
}