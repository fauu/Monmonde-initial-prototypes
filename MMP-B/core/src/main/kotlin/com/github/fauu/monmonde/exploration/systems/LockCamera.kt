package com.github.fauu.monmonde.exploration.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.github.fauu.monmonde.exploration.components.IsCameraTarget
import com.github.fauu.monmonde.shared.components.HasSprite

// TODO: Find a better way to implement systems that handle a single entity.
//       A subscription?
class LockCamera(): IteratingSystem(
        Aspect.all(HasSprite::class.java, IsCameraTarget::class.java)
    ) {
  @Wire
  private lateinit var camera: OrthographicCamera

  private lateinit var mHasSprite: ComponentMapper<HasSprite>

  override fun process(e: Int) {
    val sprite = mHasSprite[e].sprite

    camera.position.x = sprite.x + .5f
    camera.position.y = sprite.y + .5f
    camera.update()
  }
}

