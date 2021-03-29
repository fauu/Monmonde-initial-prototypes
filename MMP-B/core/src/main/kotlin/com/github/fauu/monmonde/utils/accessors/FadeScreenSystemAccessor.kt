package com.github.fauu.monmonde.utils.accessors

import aurelienribon.tweenengine.TweenAccessor
import com.github.fauu.monmonde.shared.systems.FadeScreen

class FadeScreenSystemAccessor: TweenAccessor<FadeScreen> {
  companion object {
    val TYPE_FADE_LEVEL = 1
  }

  override fun getValues(
      target: FadeScreen,
      tweenType: Int,
      returnValues: FloatArray
  ): Int {
    when (tweenType) {
      TYPE_FADE_LEVEL -> {
        returnValues[0] = target.fadeLevel
        return 1
      }
      else -> {
        assert(false)
        return -1
      }
    }
  }

  override fun setValues(
      target: FadeScreen,
      tweenType: Int,
      newValues: FloatArray
  ): Unit {
    when (tweenType) {
      TYPE_FADE_LEVEL -> target.fadeLevel = newValues[0]
      else -> assert(false)
    }
  }
}