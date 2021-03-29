package com.github.fauu.monmonde.exploration.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.GridPoint2
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.shared.components.IsPlayerControlled
import com.github.fauu.monmonde.battle.components.WillMove
import com.github.fauu.monmonde.exploration.components.IsMoving
import com.github.fauu.monmonde.utils.extensions.isZero

class HandleExplorationInput:
    IteratingSystem(
        Aspect.all(IsPlayerControlled::class.java)
              .exclude(WillMove::class.java, IsMoving::class.java)
    ) {
  private lateinit var mWillMove: ComponentMapper<WillMove>

  override fun process(e: Int) {
    val movementVector = GridPoint2()

    MOVEMENT_KEYS.forEach { entry ->
      val (key, vector) = entry
      if (Gdx.input.isKeyPressed(key)) movementVector.add(vector)
    }

    if (!movementVector.isZero) {
      val cWillMove = mWillMove.create(e)
      cWillMove.vector = movementVector.cpy()
    }
  }

  companion object {
    val MOVEMENT_KEYS = mapOf<Int, GridPoint2>(
        Input.Keys.W to GridPoint2(0, 1),
        Input.Keys.S to GridPoint2(0, -1),
        Input.Keys.A to GridPoint2(-1, 0),
        Input.Keys.D to GridPoint2(1, 0)
    )
  }
}
