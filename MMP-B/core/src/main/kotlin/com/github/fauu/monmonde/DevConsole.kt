package com.github.fauu.monmonde

import com.badlogic.gdx.graphics.g2d.Sprite
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.GUIConsole

object DevConsole {
  private val console = GUIConsole()

  private val registeredSprites = mutableMapOf<String, Sprite>()

  init {
    console.setCommandExecutor(DevCommandExecutor)
  }

  fun register(key: String, obj: Any) {
    when (obj) {
      is Sprite -> registeredSprites.put(key, obj)
      else -> throw UnsupportedOperationException("Unsupported object type")
    }
  }

  fun log(msg: String) {
    console.log(msg)
  }

  fun render() = console.draw()

  fun dispose() = console.dispose()

  object DevCommandExecutor: CommandExecutor() {
    fun testString(str: String) {
      console.log(str)
    }

    fun listSprites() {
      for ((key, sprite) in registeredSprites) {
        console.log("[$key] pos=(${sprite.x},${sprite.y}) scl=${sprite.scaleX}")
      }
    }

    fun moveSpriteTo(key: String, x: Float, y: Float) {
      val sprite = getSpriteOrError(key)

      sprite!!.setPosition(x, y)
      console.log("Moved [$key] to ($x,$y)")
    }

    fun scaleSpriteTo(key: String, scale: Float) {
      val sprite = getSpriteOrError(key)

      sprite!!.setScale(scale)
      console.log("Scaled [$key] to $scale")
    }

    private fun getSpriteOrError(key: String): Sprite? {
      val sprite = registeredSprites[key]

      if (sprite == null) console.log("No sprite registered under such key.")

      return sprite
    }
  }
}