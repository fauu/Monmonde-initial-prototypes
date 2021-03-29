package com.github.fauu.monmonde.screens.battle

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.github.fauu.monmonde.Assets
import com.github.fauu.monmonde.Mon
import com.github.fauu.monmonde.MonKind
import com.github.fauu.monmonde.Screen
import com.github.fauu.monmonde.camera.MoveAction
import com.github.fauu.monmonde.camera.ZoomAction

class BattleScreen(batch: SpriteBatch) : Screen(batch) {
  companion object {
    val ASSET_DEPENDENCIES = listOf(*Assets.BATTLE_BACKGROUNDS)
  }

  private lateinit var camera: OrthographicCamera

  /* TODO: After zooming in on the mon in exploration, white out the screen,
   *       switch to battle, transition back from white zoomed out into the mon
   *       in battle and then zoom out
   */
  override fun prepare() {
    if (ready) return

    Assets.load(ASSET_DEPENDENCIES)

    camera = OrthographicCamera()
    viewport = ScalingViewport(Scaling.none, 928f, 800f, camera)
    stage = Stage(viewport, batch)

    ready = true
  }

  override fun activate() {
    // TODO: Move to Battle
    val background = Image(Assets.get(Assets.BATTLE_BACKGROUNDS[0]))
    stage.addActor(background)

    val battle = Battle(Mon(MonKind.BULBASAUR), Mon(MonKind.PIDGEY), camera)
    stage.addActor(battle)
  }

  override fun processInput(delta: Float) = Unit

  override fun render(delta: Float) {
    camera.update()

    viewport.setScreenPosition(0, 0)
    viewport.apply()

    stage.act(delta)
    stage.draw()
  }

  override fun dispose() = super.dispose()

  override fun handleMessage(telegram: Telegram?) = true
}
