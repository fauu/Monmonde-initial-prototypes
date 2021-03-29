package com.github.fauu.monmonde.battle.components

import com.artemis.Component

class HasSpriteScaleAnimation(): Component() {
  var targetValue: Float = 0f
  var amount: Float = 0f
  var duration: Float = 0f
  var elapsed: Float = 0f

  constructor(targetValue: Float, duration: Float): this() {
    this.duration = duration
    this.targetValue = targetValue
  }
}