package com.github.fauu.monmonde.battle.components

import com.artemis.Component

class HasFatigue(): Component() {
  var fatigue = 0
  var maxReserve = 0

  val reserve: Int
      get() = maxReserve - fatigue
  val reservePercent: Float
      get() = reserve / maxReserve.toFloat()
}
