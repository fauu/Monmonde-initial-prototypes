package com.github.fauu.monmonde.battle.systems

import com.artemis.Aspect
import com.artemis.AspectSubscriptionManager
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.shared.Message
import com.github.fauu.monmonde.battle.components.*
import com.github.fauu.monmonde.shared.components.*
import com.github.fauu.monmonde.battle.types.BattleSpot
import se.feomedia.orion.OperationFactory.*
import com.github.fauu.monmonde.shared.operations.MonmondeOperations.colorize

class EffectuateBattleMoveConsequences :
    IteratingSystem(
        Aspect.all(IsAttacked::class.java, HasFatigue::class.java)
    ) {
  private val messenger = MessageManager.getInstance()

  private lateinit var mIsAttacked: ComponentMapper<IsAttacked>
  private lateinit var mHasFatigue: ComponentMapper<HasFatigue>
  private lateinit var mIsAtBattleSpot: ComponentMapper<IsAtBattleSpot>
  private lateinit var mIsUsingMove: ComponentMapper<IsUsingMove>

  // TODO: Refactor into a separate system
  private lateinit var mIsPlayerControlled: ComponentMapper<IsPlayerControlled>
  private lateinit var mIsAiControlled: ComponentMapper<IsAiControlled>
  private lateinit var aspectSubscriptionManager: AspectSubscriptionManager
  private lateinit var playerMonSubscription: EntitySubscription
  private lateinit var foeMonSubscription: EntitySubscription

  override fun initialize() {
    aspectSubscriptionManager = world.aspectSubscriptionManager
    playerMonSubscription =
        aspectSubscriptionManager.get(
            Aspect.all(IsPlayerControlled::class.java, IsAtBattleSpot::class.java))
    foeMonSubscription =
        aspectSubscriptionManager.get(Aspect.all(IsAiControlled::class.java, IsAtBattleSpot::class.java))
  }

  override fun process(e: Int) {
    val move = mIsAttacked[e].move

    mIsAttacked.remove(e)
    DevConsole.log(move.name)

    val cHasFatigue = mHasFatigue[e]
    with (cHasFatigue) {
      fatigue += move.power / 10
      fatigue = MathUtils.clamp(cHasFatigue.fatigue, 0, cHasFatigue.maxReserve)
    }

    val battleSpot = mIsAtBattleSpot[e].spot
    val monFatigueInfo = Pair(battleSpot, cHasFatigue.reservePercent)
    messenger.dispatchMessage(Message.MON_FATIGUE_CHANGED, monFatigueInfo)

    repeat(2,
      sequence(
          colorize(Color.RED, .1f),
          colorize(Color.WHITE, .1f)
      )
    ).register(world.getEntity(e))

    // TODO: Refactor into a separate system
    if (cHasFatigue.reserve == 0) {
      val playerMon = playerMonSubscription.entities[0]
      val foeMon = foeMonSubscription.entities[0]

      mIsUsingMove.remove(playerMon)
      mIsUsingMove.remove(foeMon)

      BattleSpot.values().iterator().forEach {
        messenger.dispatchMessage(Message.MON_MOVE_FINISHED, it)
      }

      messenger.dispatchMessage(
          Message.PLAYER_MOVE_ORDERING_STATE_CHANGED, false)

      mIsPlayerControlled.remove(playerMon)
      mIsAiControlled.remove(foeMon)
    }
  }
}

