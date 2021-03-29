package com.github.fauu.monmonde.shared.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.github.fauu.monmonde.battle.components.HasColorization
import com.github.fauu.monmonde.shared.components.HasSprite

class ColorizeSprites:
    IteratingSystem(
        Aspect.all(HasSprite::class.java, HasColorization::class.java)
    ) {
  private lateinit var mHasSprite: ComponentMapper<HasSprite>
  private lateinit var mHasColorization: ComponentMapper<HasColorization>

  override fun process(e: Int) {
    val sprite = mHasSprite[e].sprite
    val color = mHasColorization[e].colorization

    sprite.color = color
  }
}