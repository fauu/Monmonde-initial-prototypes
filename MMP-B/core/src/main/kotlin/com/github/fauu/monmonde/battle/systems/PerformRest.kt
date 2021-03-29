package com.github.fauu.monmonde.battle.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.DelayedIteratingSystem
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.battle.components.IsUsingMove
import com.github.fauu.monmonde.battle.components.IsResting

class PerformRest():
    DelayedIteratingSystem(
        Aspect.all(IsResting::class.java)
    ) {
  private lateinit var mIsResting: ComponentMapper<IsResting>

  override fun processDelta(e: Int, accumulatedDelta: Float) {
    val cIsResting = mIsResting.get(e)
    cIsResting.timeLeft -= accumulatedDelta
  }

  override fun getRemainingDelay(e: Int): Float {
    val cIsResting = mIsResting.get(e)
    return cIsResting.timeLeft
  }

  override fun processExpired(e: Int) {
    mIsResting.remove(e)
  }
}
