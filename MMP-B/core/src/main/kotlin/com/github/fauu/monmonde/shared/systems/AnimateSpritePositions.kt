package com.github.fauu.monmonde.shared.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.shared.components.HasSprite
import com.github.fauu.monmonde.battle.components.HasSpritePositionAnimation

class AnimateSpritePositions:
    IteratingSystem(
        Aspect.all(HasSpritePositionAnimation::class.java)
    ) {
  private lateinit var mHasSpritePositionAnimation:
      ComponentMapper<HasSpritePositionAnimation>
  private lateinit var mHasSprite: ComponentMapper<HasSprite>

  override fun process(e: Int) {
    val cHasSpritePositionAnimation = mHasSpritePositionAnimation[e]
    val sprite = mHasSprite[e].sprite

    with (cHasSpritePositionAnimation) {
      if (amount.isZero) {
        amount = targetValue.cpy().sub(sprite.x, sprite.y)
      } else if (targetValue.isZero) {
        targetValue = Vector2(sprite.x + amount.x, sprite.y + amount.y)
      }

      elapsed += Gdx.graphics.deltaTime

      val newValue =
        if (elapsed < duration) {
          val percentComplete = elapsed / duration
          targetValue.cpy().sub(amount.cpy().scl(1 - percentComplete))
        } else {
          mHasSpritePositionAnimation.remove(e)
          targetValue
        }

      sprite.setPosition(newValue.x, newValue.y)
    }
  }
}