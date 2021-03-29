package com.github.fauu.monmonde.battle.components

import com.artemis.Component
import com.github.fauu.monmonde.shared.types.MonMove

class HasMoves(): Component() {
  val moves = mutableListOf<MonMove>()
}