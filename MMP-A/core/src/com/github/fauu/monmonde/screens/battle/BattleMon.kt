package com.github.fauu.monmonde.screens.battle

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.fauu.monmonde.Mon

class BattleMon(private val mon: Mon, position: Battle.Position): Image() {
  init {
    val tr = when (position) {
      Battle.Position.PLAYER -> mon.kind.battleTRBack
      Battle.Position.FOE -> mon.kind.battleTRFront
    }

    drawable = TextureRegionDrawable(tr)
  }
}