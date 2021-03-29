package com.github.fauu.monmonde.shared.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.github.fauu.monmonde.shared.components.HasSprite
import com.github.fauu.monmonde.battle.components.HasSpriteScaleAnimation

class AnimateSpriteScales:
    IteratingSystem(
      Aspect.all(HasSpriteScaleAnimation::class.java)
    ) {
  private lateinit var mHasSpriteScaleAnimation:
      ComponentMapper<HasSpriteScaleAnimation>
  private lateinit var mHasSprite: ComponentMapper<HasSprite>

  override fun process(e: Int) {
    val cHasSpriteScaleAnimation = mHasSpriteScaleAnimation[e]
    val sprite = mHasSprite[e].sprite

    with (cHasSpriteScaleAnimation) {
      if (amount == 0f) {
        amount = targetValue - sprite.scaleX
      }

      elapsed += Gdx.graphics.deltaTime

      val newValue =
        if (elapsed < duration) {
          val percentComplete = elapsed / duration
          targetValue - (amount * (1 - percentComplete))
        } else {
          mHasSpriteScaleAnimation.remove(e)
          targetValue
        }

      sprite.setScale(newValue)
    }
  }
}