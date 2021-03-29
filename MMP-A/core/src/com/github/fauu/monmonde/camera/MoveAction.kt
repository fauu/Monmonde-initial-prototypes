package com.github.fauu.monmonde.camera

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction

class MoveAction(private val camera: OrthographicCamera,
                 private val targetValue: Vector2,
                 duration: Float = 0f,
                 interpolation: Interpolation = Interpolation.linear)
    : TemporalAction(duration, interpolation) {
  private var amount = Vector2()

  override fun begin() {
    amount.add(targetValue.cpy().sub(camera.position.x, camera.position.y))
  }

  override fun update(percent: Float) {
    camera.position.set(targetValue.x - (amount.x * (1f - percent)),
                        targetValue.y - (amount.y * (1f - percent)),
                        0f)
  }
}
