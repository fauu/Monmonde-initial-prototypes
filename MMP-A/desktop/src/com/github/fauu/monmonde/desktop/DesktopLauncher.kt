package com.github.fauu.monmonde.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.fauu.monmonde.Monmonde

public fun main(arg: Array<String>?) {
  val config = LwjglApplicationConfiguration()
  config.width = 1280
  config.height = 800
	/*
  config.vSyncEnabled = false
	config.foregroundFPS = 0
	config.backgroundFPS = 0
  */

  LwjglApplication(Monmonde(), config)
}