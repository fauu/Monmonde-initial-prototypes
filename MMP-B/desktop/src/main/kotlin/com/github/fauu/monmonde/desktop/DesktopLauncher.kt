package com.github.fauu.monmonde.desktop

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.fauu.monmonde.Monmonde

/** Launches the desktop (LWJGL) application.  */
object DesktopLauncher {
  @JvmStatic fun main(args: Array<String>) {
    createApplication()
  }

  private fun createApplication(): LwjglApplication {
    return LwjglApplication(Monmonde(), defaultConfiguration)
  }

  private val defaultConfiguration: LwjglApplicationConfiguration
    get() {
      val configuration = LwjglApplicationConfiguration()
      configuration.title = "Monmonde"
      configuration.width = 1280
      configuration.height = 800
      for (size in intArrayOf(128, 64, 32, 16)) {
        configuration.addIcon("libgdx$size.png", FileType.Internal)
      }
      return configuration
    }
}