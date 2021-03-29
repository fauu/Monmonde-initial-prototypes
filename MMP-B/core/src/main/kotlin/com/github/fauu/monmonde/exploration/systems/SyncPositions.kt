package com.github.fauu.monmonde.exploration.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.github.fauu.monmonde.exploration.components.HasPosition
import com.github.fauu.monmonde.exploration.components.IsMoving
import com.github.fauu.monmonde.shared.components.HasSprite

class SyncPositions(): IteratingSystem(
        Aspect.all(HasSprite::class.java, HasPosition::class.java)
              .exclude(IsMoving::class.java)
    ) {
  private lateinit var mHasSprite: ComponentMapper<HasSprite>
  private lateinit var mHasPosition: ComponentMapper<HasPosition>

  override fun process(e: Int) {
    val sprite = mHasSprite[e].sprite
    val position = mHasPosition[e].position

    sprite.setPosition(position.x.toFloat(), position.y.toFloat())
  }
}