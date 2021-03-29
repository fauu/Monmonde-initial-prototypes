package com.github.fauu.monmonde.shared.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.github.fauu.monmonde.shared.components.HasSprite
import com.github.fauu.monmonde.battle.components.HasSpriteFrameAnimation

class AnimateSpriteFrames:
    IteratingSystem(
        Aspect.all(HasSpriteFrameAnimation::class.java)
    ) {
  private lateinit var mHasSprite: ComponentMapper<HasSprite>
  private lateinit var mHasSpriteFrameAnimation: ComponentMapper<HasSpriteFrameAnimation>

  override fun process(e: Int) {
    val cSpriteAnimation = mHasSpriteFrameAnimation[e]
    val cRenderable = mHasSprite[e]

    with (cSpriteAnimation) {
      time += Gdx.graphics.deltaTime

      val frameNo = (time / frameDuration).toInt() % (frames.size - 1)
      val region = cSpriteAnimation.frames[frameNo]

      cRenderable.sprite.setRegion(region)
    }
  }
}