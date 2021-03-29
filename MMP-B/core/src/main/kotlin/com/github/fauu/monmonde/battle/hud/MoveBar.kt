package com.github.fauu.monmonde.battle.hud

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.fauu.monmonde.shared.Assets

class MoveBar(private val font: BitmapFont): Actor() {
  private val moveNameGlyphLayout = GlyphLayout()
  var text: String
    get() = moveNameGlyphLayout.toString()
    set(value) = moveNameGlyphLayout.setText(font, value)

  override fun draw(batch: Batch?, parentAlpha: Float) {
    if (scaleX > 0f) { // Hack
        NINEPATCH.color.a = this.color.a
        NINEPATCH.draw(batch, x, y, scaleX, scaleY)
    }

    font.draw(batch,
        moveNameGlyphLayout,
        x + (width / 2) - (moveNameGlyphLayout.width / 2),
        y + 16)
  }

  companion object {
    val NINEPATCH = NinePatch(
        Assets.TEXTURE_ATLAS.findRegion(
            "battle/hud/mon-plate/move-bar"), 1, 1, 1, 1)
  }
}