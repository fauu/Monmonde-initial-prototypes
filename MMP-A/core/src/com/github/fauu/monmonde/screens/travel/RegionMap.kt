package com.github.fauu.monmonde.screens.travel

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array

class RegionMap(texture: Texture, locations: Array<RegionLocation>) : Actor() {
	val locations = locations

	internal lateinit var sprite: Sprite

	init {
		sprite = Sprite(texture);
		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),
				sprite.getHeight())
	}

	override protected fun positionChanged() {
		sprite.setPosition(getX(), getY())
		super.positionChanged()
	}

	override fun draw(batch: Batch, parentAlpha: Float) {
		sprite.draw(batch)
	}

	override fun act(delta: Float) {
		super.act(delta)
	}
}