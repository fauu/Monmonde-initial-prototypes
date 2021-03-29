package com.github.fauu.monmonde

import com.badlogic.gdx.graphics.g2d.TextureRegion

enum class MonKind(val no: Int, val displayName: String) {
  BULBASAUR(1, "Bulbasaur"),
  PIDGEY(16, "Pidgey");

  lateinit var uiTR: TextureRegion
  lateinit var battleTRFront: TextureRegion
  lateinit var battleTRBack: TextureRegion

  override fun toString() = displayName
}
