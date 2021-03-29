package com.github.fauu.monmonde.screens.travel

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.GlyphLayout

class RegionLocationLabel(text: String, skin: Skin) : Actor() {
	internal var text = text;
	internal var skin = skin;
	internal var selected = false
	  set(value) {
		  field = value
		  
		  if (selected) {
        background = skin.getDrawable("button-down");
      } else {
        background = skin.getDrawable("check-off");
      }
    }
	internal var background = skin.getDrawable("check-off")
	internal var font = skin.getFont("bold-font")
	internal var layout = GlyphLayout()
	
	init {
		layout.setText(font, text)
	}
	
	override protected fun positionChanged() {
		super.positionChanged()
	}

	override fun draw(batch: Batch, parentAlpha: Float) {
		background.draw(batch, getX(), getY(), layout.width + 10, layout.height + 3)
		font.draw(batch, text, getX() + 7, getY() + 31)
	}

	override fun act(delta: Float) {
		super.act(delta)
	}
}