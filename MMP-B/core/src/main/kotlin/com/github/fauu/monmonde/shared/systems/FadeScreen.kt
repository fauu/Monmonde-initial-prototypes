package com.github.fauu.monmonde.shared.systems

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShaderProgram

class FadeScreen(): BaseSystem() {
  var fadeLevel = 0f

  @Wire
  private lateinit var shader: ShaderProgram

  fun setColor(color: Color) {
    val color4fv = floatArrayOf(color.r, color.g, color.b, 1f)

    shader.begin()
    shader.setUniform4fv("u_fadeColor", color4fv, 0, 4)
    shader.end()
  }

  override fun processSystem() {
    if (fadeLevel == 0f) return

    shader.begin()
    shader.setUniformf("u_fadeLevel", fadeLevel)
    shader.end()
  }
}