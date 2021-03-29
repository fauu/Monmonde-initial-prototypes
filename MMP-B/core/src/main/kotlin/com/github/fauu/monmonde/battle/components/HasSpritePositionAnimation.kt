package com.github.fauu.monmonde.battle.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

class HasSpritePositionAnimation(): Component() {
  var targetValue = Vector2()
  var amount = Vector2()
  var duration: Float = 0f
  var elapsed: Float = 0f
}