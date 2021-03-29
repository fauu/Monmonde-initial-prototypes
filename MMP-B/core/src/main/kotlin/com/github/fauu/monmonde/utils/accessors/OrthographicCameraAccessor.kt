package com.github.fauu.monmonde.utils.accessors

import aurelienribon.tweenengine.TweenAccessor
import com.badlogic.gdx.graphics.OrthographicCamera

class OrthographicCameraAccessor: TweenAccessor<OrthographicCamera> {
  companion object {
    val TYPE_XY = 1
    val TYPE_ZOOM = 2
  }

  override fun getValues(
      target: OrthographicCamera,
      tweenType: Int,
      returnValues: FloatArray
  ): Int {
    when (tweenType) {
      TYPE_XY -> {
        returnValues[0] = target.position.x
        returnValues[1] = target.position.y
        return 2
      }
      TYPE_ZOOM -> {
        returnValues[0] = target.zoom
        return 1
      }
      else -> {
        assert(false)
        return -1
      }
    }
  }

  override fun setValues(
      target: OrthographicCamera,
      tweenType: Int,
      newValues: FloatArray
  ): Unit {
    when (tweenType) {
      TYPE_XY -> {
        target.position.x = newValues[0]
        target.position.y = newValues[1]
      }
      TYPE_ZOOM -> target.zoom = newValues[0]
      else -> assert(false)
    }
  }
}