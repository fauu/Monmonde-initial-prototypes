package com.github.fauu.monmonde.utils.extensions

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

fun GridPoint2.toVector2(): Vector2 {
  return Vector2(this.x.toFloat(), this.y.toFloat())
}

val GridPoint2.isZero: Boolean
  get() = this.x == 0 && this.y == 0

val GridPoint2.isOnAnyAxis: Boolean
  get() = this.x == 0 || this.y == 0
