package com.github.fauu.monmonde.shared.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.fauu.monmonde.shared.components.IsHidden
import com.github.fauu.monmonde.shared.components.HasSprite
import com.github.fauu.monmonde.shared.systems.DeferredEntityProcessingSystem
import com.github.fauu.monmonde.shared.systems.RenderBatchingSystem

class RenderSprites(private val renderBatchingSystem: RenderBatchingSystem):
    DeferredEntityProcessingSystem(
        Aspect.all(HasSprite::class.java)
              .exclude(IsHidden::class.java),
        renderBatchingSystem
    ) {
  private lateinit var batch: SpriteBatch

  private lateinit var mHasSprite: ComponentMapper<HasSprite>

  override fun initialize() {
    batch = renderBatchingSystem.spriteBatch
  }

  override fun process(e: Int) {
    mHasSprite[e].sprite.draw(batch)
  }
}