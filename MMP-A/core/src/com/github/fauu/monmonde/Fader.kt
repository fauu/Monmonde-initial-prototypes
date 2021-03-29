package com.github.fauu.monmonde

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Disposable

class Fader(private val batch: SpriteBatch): Disposable {
  private val fades = mutableMapOf<Color, Texture>()

  private val pixmap =
      Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGBA8888)

  private var activeFadeAction: FadeAction? = null

  private val camera = OrthographicCamera()

  private var fadeLevel = 0f

  init {
    camera.setToOrtho(false)

    prepareFade(Color.BLACK)
    prepareFade(Color.WHITE)
    pixmap.dispose()
  }

  private fun prepareFade(color: Color) {
    pixmap.setColor(color)
    pixmap.fillRectangle(0, 0, Gdx.graphics.width, Gdx.graphics.height)
    val fade = Texture(pixmap)

    fades.put(color, fade)
  }

  fun run(action: FadeAction) {
    activeFadeAction = action
  }

  fun render(delta: Float) =
    activeFadeAction?.let {
      with (activeFadeAction!!) {
        elapsed += delta

        if (elapsed >= duration && type == FadeType.FADE_IN)
          activeFadeAction = null

        fadeLevel = when (type) {
          FadeType.FADE_OUT -> interpolation.apply(elapsed / duration)
          FadeType.FADE_IN -> interpolation.apply(1f - (elapsed / duration))
        }

        fadeLevel = MathUtils.clamp(fadeLevel, 0f, 1f)

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)

        val fade = fades[color]
        with (batch) {
          projectionMatrix = camera.combined
          camera.update()

          begin()
          val colorCopy = batch.color
          val oldAlpha = colorCopy.a
          colorCopy.a = fadeLevel
          color = colorCopy
          draw(fade, 0f, 0f)
          colorCopy.a = oldAlpha
          color = colorCopy
          end()
        }
      }
    }

  override fun dispose() = pixmap.dispose()

  data class FadeAction(val type: FadeType,
                        val color: Color,
                        val duration: Float,
                        val interpolation: Interpolation = Interpolation.linear)
  {
    var elapsed = 0f
  }

  enum class FadeType {
    FADE_OUT, FADE_IN
  }
}