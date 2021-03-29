package com.github.fauu.monmonde.sim.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.fauu.monmonde.sim.MonmondeSim

object DesktopLauncher {
  @JvmStatic
  fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration().apply {
      foregroundFPS = 30
      width = 1400
      height = 900
    }
    LwjglApplication(MonmondeSim(), config)
  }
}
