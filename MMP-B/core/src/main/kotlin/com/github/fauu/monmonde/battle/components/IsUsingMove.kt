package com.github.fauu.monmonde.battle.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.annotations.PooledWeaver
import com.github.fauu.monmonde.shared.types.MonMove

@PooledWeaver
class IsUsingMove(): Component() {
  @EntityId var target = -1
  lateinit var move: MonMove
  var timeLeft = 0f
}