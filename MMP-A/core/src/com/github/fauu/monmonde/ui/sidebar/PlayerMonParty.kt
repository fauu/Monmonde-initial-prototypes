package com.github.fauu.monmonde.ui.sidebar

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.fauu.monmonde.MonKind

class PlayerMonParty(skin: Skin) : Table(skin) {
  init {
    defaults().bottom().left().expandX()

    val header = Label("Party:", skin, "small-font", Color.WHITE)
    add(header)

    row()
    add(Entry(skin))
  }

  class Entry(skin: Skin): Table(skin) {
    init {
      val image = Image(MonKind.BULBASAUR.uiTR)
      add(image).size(32f, 32f).spaceRight(10f)

      val label = Label(MonKind.BULBASAUR.displayName, skin, "small-font", Color.WHITE)
      add(label).padBottom(7f)
    }
  }
}
