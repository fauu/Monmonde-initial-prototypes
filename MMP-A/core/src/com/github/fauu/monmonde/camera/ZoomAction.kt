package com.github.fauu.monmonde.camera

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction

class ZoomAction(private val camera: OrthographicCamera,
                 private val targetValue: Float,
                 duration: Float = 0f,
                 interpolation: Interpolation = Interpolation.linear)
    : TemporalAction(duration, interpolation) {
  private var amount = 0f

  override fun begin() {
    amount = camera.zoom - targetValue
  }

  override fun update(percent: Float) {
    camera.zoom = targetValue + (amount * (1f - percent))
  }
}
