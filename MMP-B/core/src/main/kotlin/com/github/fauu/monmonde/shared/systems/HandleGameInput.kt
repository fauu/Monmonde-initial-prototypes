package com.github.fauu.monmonde.shared.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class HandleGameInput: BaseSystem() {
  override fun processSystem() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      Gdx.app.exit()
    }
  }
}