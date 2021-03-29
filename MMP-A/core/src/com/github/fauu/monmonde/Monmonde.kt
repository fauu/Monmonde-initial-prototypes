package com.github.fauu.monmonde

import com.badlogic.gdx.*
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.fauu.monmonde.ui.sidebar.Sidebar
import com.github.fauu.monmonde.screens.exploration.ExplorationScreen
import com.github.fauu.monmonde.screens.travel.TravelScreen
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.fauu.monmonde.screens.battle.BattleScreen

// TODO: Switch to a single stage (or at most two - main and UI)
class Monmonde : ApplicationAdapter() {
  companion object {
    val ASSET_DEPENDENCIES =
        listOf(Assets.SKIN, Assets.UI_SPRITES, Assets.BATTLE_SPRITES)
  }

  private var messenger = MessageManager.getInstance()

  private lateinit var sidebar: Sidebar

	private val fpsLogger = FPSLogger()

  private var exploration = false

  private lateinit var batch: SpriteBatch

  private lateinit var fader: Fader

  private lateinit var screenManager: ScreenManager

	override fun create() {
    Gdx.app.logLevel = Application.LOG_DEBUG

    Assets.load(ASSET_DEPENDENCIES)

    val uiSpritesTexture = Assets.get(Assets.UI_SPRITES)
    for (monKind in MonKind.values()) {
      val no = monKind.no - 1
      monKind.uiTR =
          TextureRegion(uiSpritesTexture, (no % 16) * 32, (no / 16) * 32, 32, 32)
    }

    val battleSpritesTexture = Assets.get(Assets.BATTLE_SPRITES)
    for (monKind in MonKind.values()) {
      var no = (monKind.no - 1) * 2
      monKind.battleTRFront =
          TextureRegion(battleSpritesTexture, (no % 21) * 96, (no / 21) * 96, 96, 96)
      no += 1
      monKind.battleTRBack =
          TextureRegion(battleSpritesTexture, (no % 21) * 96, (no / 21) * 96, 96, 96)
    }

		val skin = Assets.get(Assets.SKIN)
    sidebar = Sidebar(skin)

    batch = SpriteBatch()

    fader = Fader(batch)

    screenManager = ScreenManager(fader)
    screenManager.register(TravelScreen(batch, skin))
    screenManager.register(ExplorationScreen(batch))
    screenManager.register(BattleScreen(batch))
    screenManager.set(TravelScreen::class.java)

    Gdx.input.inputProcessor = object: InputAdapter() {
      override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
          Input.Keys.T -> {
            if (!exploration) {
              exploration = true
              messenger.dispatchMessage(Message.ACTIVE_SCREEN_CHANGE_REQUESTED, ExplorationScreen::class.java)
            } else {
              exploration = false
              messenger.dispatchMessage(Message.ACTIVE_SCREEN_CHANGE_REQUESTED, TravelScreen::class.java)
            }
          }
          Input.Keys.ESCAPE -> {
            Gdx.app.exit()
          }
        }
        return true;
      }
    }
	}

	override fun resize(width: Int, height: Int) = super.resize(width, height)

	override fun render() {
    val delta = Gdx.graphics.deltaTime

    GdxAI.getTimepiece().update(delta)
    messenger.update()

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		sidebar.render(delta)

    screenManager.activeScreen?.render(delta)

    fader.render(delta)

		//fpsLogger.log()
	}

	override fun dispose() {
    Assets.dispose()
    batch.dispose()

    super.dispose()
  }
}