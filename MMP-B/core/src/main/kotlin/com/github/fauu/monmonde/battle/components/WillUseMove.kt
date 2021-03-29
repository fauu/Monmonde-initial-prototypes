package com.github.fauu.monmonde.battle.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.annotations.PooledWeaver
import com.github.fauu.monmonde.shared.types.MonMove

@PooledWeaver
class WillUseMove(): Component() {
  @EntityId var target = -1
  lateinit var move: MonMove

  constructor(target: Int, move: MonMove): this() {
    this.target = target
    this.move = move
  }
}

