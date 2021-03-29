package com.github.fauu.monmonde

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport

abstract class Screen(protected val batch: SpriteBatch)
    : Telegraph {
  companion object {
    const val WIDTH = 928f
    const val HEIGHT = 800f
  }

  protected var ready = false

  protected val messenger = MessageManager.getInstance()

  lateinit var stage: Stage
    protected set

  protected lateinit var viewport: Viewport

  abstract fun prepare()

  abstract fun activate()

  abstract protected fun processInput(delta: Float)

  abstract fun render(delta: Float)

  open fun dispose() = stage.dispose()
}