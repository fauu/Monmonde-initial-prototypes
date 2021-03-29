package com.github.fauu.monmonde.battle.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.github.fauu.monmonde.shared.Message
import com.github.fauu.monmonde.battle.components.IsAtBattleSpot
import com.github.fauu.monmonde.battle.components.IsResting
import com.github.fauu.monmonde.battle.components.IsUsingMove
import com.github.fauu.monmonde.battle.components.WillUseMove
import com.github.fauu.monmonde.shared.components.*

class StartOrderedBattleMoves():
    IteratingSystem(
        Aspect.all(WillUseMove::class.java)
              .exclude(IsResting::class.java)
    ) {
  private val messenger = MessageManager.getInstance()

  private lateinit var mWillUseMove: ComponentMapper<WillUseMove>
  private lateinit var mIsUsingMove: ComponentMapper<IsUsingMove>
  private lateinit var mIsAtBattleSpot: ComponentMapper<IsAtBattleSpot>
  private lateinit var mIsPlayerControlled: ComponentMapper<IsPlayerControlled>

  override fun process(e: Int) {
    val target = mWillUseMove[e].target
    val move = mWillUseMove[e].move
    val timeLeft = move.executionTime

    mWillUseMove.remove(e)

    val cIsUsingMove = mIsUsingMove.create(e)
    cIsUsingMove.target = target
    cIsUsingMove.move = move
    cIsUsingMove.timeLeft = timeLeft

    val battleSpot = mIsAtBattleSpot[e].spot
    val moveInfo = Pair(battleSpot, move)
    messenger.dispatchMessage(Message.MON_MOVE_BEGAN, moveInfo)

    if (mIsPlayerControlled.has(e))
      messenger.dispatchMessage(
          Message.PLAYER_MOVE_ORDERING_STATE_CHANGED, false)
  }
}
