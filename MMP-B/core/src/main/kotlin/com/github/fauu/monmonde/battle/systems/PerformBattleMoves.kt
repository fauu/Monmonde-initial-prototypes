package com.github.fauu.monmonde.battle.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.DelayedIteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.shared.Message
import com.github.fauu.monmonde.battle.components.IsAtBattleSpot
import com.github.fauu.monmonde.battle.components.IsAttacked
import com.github.fauu.monmonde.battle.components.IsUsingMove
import com.github.fauu.monmonde.shared.components.*

class PerformBattleMoves():
    DelayedIteratingSystem(
        Aspect.all(IsUsingMove::class.java)
    ) {
  private val messenger = MessageManager.getInstance()

  private lateinit var mIsUsingMove: ComponentMapper<IsUsingMove>
  private lateinit var mIsAttacked: ComponentMapper<IsAttacked>
  private lateinit var mIsPlayerControlled: ComponentMapper<IsPlayerControlled>
  private lateinit var mIsAtBattleSpot: ComponentMapper<IsAtBattleSpot>

  override fun processDelta(e: Int, accumulatedDelta: Float) {
    val cIsUsingMove = mIsUsingMove.get(e)
    cIsUsingMove.timeLeft -= accumulatedDelta
  }

  override fun getRemainingDelay(e: Int): Float {
    val cIsUsingMove = mIsUsingMove.get(e)
    return cIsUsingMove.timeLeft
  }

  override fun processExpired(e: Int) {
    val target = mIsUsingMove[e].target
    val move = mIsUsingMove[e].move

    val cIsAttacked = mIsAttacked.create(target)
    cIsAttacked.move = move

    mIsUsingMove.remove(e)

    val battleSpot = mIsAtBattleSpot[e].spot
    messenger.dispatchMessage(Message.MON_MOVE_FINISHED, battleSpot)

    if (mIsPlayerControlled.has(e))
      messenger.dispatchMessage(
          Message.PLAYER_MOVE_ORDERING_STATE_CHANGED, true)
  }
}

