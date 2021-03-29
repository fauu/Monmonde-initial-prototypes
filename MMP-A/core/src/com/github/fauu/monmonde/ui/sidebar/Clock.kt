package com.github.fauu.monmonde.ui.sidebar

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class Clock(uiSkin: Skin) : Table() {
	init {
    val date = Label("Wednesday 3 August 2016", uiSkin, "small-font", Color.WHITE)
    add(date).left().expandX()

    val time = Label("17:30", uiSkin, "small-font", Color.WHITE)
    add(time).right().expandX()
	}
}