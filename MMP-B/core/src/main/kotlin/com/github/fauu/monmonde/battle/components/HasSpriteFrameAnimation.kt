package com.github.fauu.monmonde.battle.components

import com.artemis.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class HasSpriteFrameAnimation(): Component() {
  var time: Float = 0f
  var frameDuration: Float = 0f
  lateinit var frames: List<TextureRegion>
}
