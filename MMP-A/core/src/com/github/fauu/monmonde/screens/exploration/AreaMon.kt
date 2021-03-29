package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.fauu.monmonde.Mon

class AreaMon(val mon: Mon,
              override val worldPosition: GridPoint2,
              spritesTexture: Texture)
    : Actor(), AreaEntity {
  val displayName: String
    get() = mon.kind.displayName

  lateinit var sprite: Sprite

  init {
    val no = mon.kind.no
    // TODO: Factor out
    val region = TextureRegion(spritesTexture, (no % 16) * 16, (no / 16) * 16, 16, 16)
    sprite = Sprite(region)
    sprite.x = worldPosition.x * 16f
    sprite.y = worldPosition.y * 16f
    setBounds(sprite.x, sprite.y, sprite.width, sprite.height)
  }

  override fun positionChanged() {
    sprite.setPosition(x, y)
    super.positionChanged()
  }

  override fun draw(batch: Batch, parentAlpha: Float) {
    sprite.draw(batch)
  }

  override fun act(delta: Float) {
    super.act(delta)
  }
}
