package com.github.fauu.monmonde.battle.systems

import com.artemis.Aspect
import com.artemis.AspectSubscriptionManager
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.systems.IteratingSystem
import com.github.fauu.monmonde.battle.components.*
import com.github.fauu.monmonde.shared.components.*
import java.util.*

class OrderAiBattleMove():
    IteratingSystem(
        Aspect.all(IsAiControlled::class.java,
                   HasMoves::class.java)
              .exclude(IsUsingMove::class.java,
                       IsResting::class.java)
    ) {
  private lateinit var aspectSubscriptionManager: AspectSubscriptionManager
  private lateinit var playerMonSubscription: EntitySubscription

  private lateinit var mWillUseMove: ComponentMapper<WillUseMove>
  private lateinit var mHasMoves: ComponentMapper<HasMoves>

  private val random = Random()

  override fun initialize() {
    aspectSubscriptionManager = world.aspectSubscriptionManager
    playerMonSubscription =
        aspectSubscriptionManager.get(
            Aspect.all(IsPlayerControlled::class.java,
                IsAtBattleSpot::class.java))
  }

  override fun process(e: Int) {
    val playerControlledId = playerMonSubscription.entities[0]
    val moves = mHasMoves[e].moves

    val cWillUseMove = mWillUseMove.create(e)
    cWillUseMove.target = playerControlledId
    cWillUseMove.move = moves[random.nextInt(moves.size)]
  }
}

