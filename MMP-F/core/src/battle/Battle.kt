package com.github.fauu.monmonde.sim.battle

import com.github.fauu.monmonde.sim.actor.Trainer
import com.github.fauu.monmonde.sim.util.Random
import com.github.fauu.monmonde.sim.util.nextGaussian
import kotlin.math.roundToInt

class Battle(private val trainers: Pair<Trainer, Trainer>) {
  fun perform(): BattleResult {
    trainers.run {
      val effectiveBattlePowers = listOf(first, second).map {
        it.battlePower + Random().nextGaussian(0.0, 30.0).roundToInt()
      }
      return if (effectiveBattlePowers[0] > effectiveBattlePowers[1]) {
        BattleResult(first, second)
      } else {
        BattleResult(second, first)
      }
    }
  }
}