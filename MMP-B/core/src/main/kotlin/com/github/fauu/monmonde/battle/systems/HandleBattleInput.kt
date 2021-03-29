package com.github.fauu.monmonde.battle.systems

import com.artemis.Aspect
import com.artemis.AspectSubscriptionManager
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.battle.components.*
import com.github.fauu.monmonde.shared.components.*

class HandleBattleInput():
    IteratingSystem(
        Aspect.all(IsPlayerControlled::class.java, HasMoves::class.java)
              .exclude(IsUsingMove::class.java)
    ) {
  private lateinit var aspectSubscriptionManager: AspectSubscriptionManager
  private lateinit var foeSubscription: EntitySubscription

  private lateinit var mHasMoves: ComponentMapper<HasMoves>
  private lateinit var mWillUseMove: ComponentMapper<WillUseMove>

  override fun initialize() {
    aspectSubscriptionManager = world.aspectSubscriptionManager
    foeSubscription = aspectSubscriptionManager.get(
        Aspect.all(IsAiControlled::class.java, IsAtBattleSpot::class.java))
  }

  override fun process(e: Int) {
    val moves = mHasMoves[e].moves

    moves.forEachIndexed { i, move ->
      if (Gdx.input.isKeyJustPressed(KEYS[i])) {
        val moveCommand = mWillUseMove.create(e)

        moveCommand.target = foeSubscription.entities[0]
        moveCommand.move = move
      }
    }
  }

  companion object {
    val KEYS = listOf(Input.Keys.Q, Input.Keys.W, Input.Keys.E, Input.Keys.R)
  }
}