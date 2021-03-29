package com.github.fauu.monmonde.battle.hud

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.fauu.monmonde.shared.Assets
import com.github.fauu.monmonde.Monmonde
import com.github.fauu.monmonde.shared.types.MonMove

class MonPlate(private val monNameFont: BitmapFont,
               monMoveBarFont: BitmapFont,
               private val monName: String): Group() {
  private val fatigueReserveBar = FatigueReserveBar()
  private var moveBar = MoveBar(monMoveBarFont)

  init {
    with (fatigueReserveBar) {
      x += 2
      y -= 32
      scaleY = 11f
      scaleX = BAR_WIDTH
    }
    addActor(fatigueReserveBar)

    with (moveBar) {
      x += 2
      y -= 57
      width = BAR_WIDTH
      scaleY = 22f
      scaleX = 0f
      color.a = 1f
    }
    moveBar.text = "Holding"
    addActor(moveBar)
  }

  fun updateFatigueBar(fatigueReservePercent: Float) {
    // TODO: Constant scaling rate?
    fatigueReserveBar.addAction(
        Actions.scaleTo(fatigueReservePercent * BAR_WIDTH,
                        fatigueReserveBar.scaleY, .3f))
  }

  fun displayMove(move: MonMove) {
    moveBar.text = move.toString()
    moveBar.scaleX = 0f
    moveBar.color.a = 1f
    moveBar.addAction(Actions.scaleTo(BAR_WIDTH, moveBar.scaleY, move.executionTime))
  }

  fun displayRest(restTime: Float) {
    moveBar.text = "Resting"
    moveBar.addAction(Actions.alpha(0f, restTime))
  }

  fun displayHold() {
    moveBar.text = "Holding"
    moveBar.color.a = 0f
  }

  override fun draw(batch: Batch?, parentAlpha: Float) {
    monNameFont.draw(batch, monName, x, y)

    BG_NINEPATCH.draw(batch, x - 1, y - 60, WIDTH, HEIGHT)

    super.draw(batch, parentAlpha)
  }

  companion object {
    val WIDTH = 200f
    val HEIGHT = 42f
    val BAR_WIDTH = WIDTH - 6f

    val BG_NINEPATCH = NinePatch(Assets.TEXTURE_ATLAS.findRegion(
            "battle/hud/mon-plate/bg"), 4, 4, 4, 4)
  }
}