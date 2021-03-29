package com.github.fauu.monmonde.ui.sidebar

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class Sidebar(skin: Skin): Table(skin) {
  init {
    val orderMovePanel = OrderMovePanel(skin)
    add(orderMovePanel).expandX().fillX().center()

    pack()
  }
}