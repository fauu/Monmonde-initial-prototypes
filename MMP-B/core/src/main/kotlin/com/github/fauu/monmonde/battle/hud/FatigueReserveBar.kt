package com.github.fauu.monmonde.battle.hud

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.fauu.monmonde.shared.Assets

class FatigueReserveBar: Actor() {
  override fun draw(batch: Batch?, parentAlpha: Float) {
    if (scaleX > 0f) { // Hack
      NINEPATCH.draw(batch, x, y, scaleX, scaleY)
    }
  }

  companion object {
    val NINEPATCH = NinePatch(
        Assets.TEXTURE_ATLAS.findRegion(
            "battle/hud/mon-plate/fatigue-reserve-bar"), 1, 1, 1, 1)

  }
}