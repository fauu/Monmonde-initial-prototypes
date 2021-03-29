package com.github.fauu.monmonde.shared.components

import com.artemis.Component
import com.badlogic.gdx.graphics.g2d.Sprite

class HasSprite(): Component() {
  lateinit var sprite: Sprite
  var zIndex: Int = 0
}
