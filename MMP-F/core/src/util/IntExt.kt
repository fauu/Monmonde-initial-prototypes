package com.github.fauu.monmonde.sim.util

// https://github.com/czyzby/kotlin-times
inline infix operator fun Int.times(action: (Int) -> Unit) {
  var i = 0
  while (i < this) action(i++)
}
