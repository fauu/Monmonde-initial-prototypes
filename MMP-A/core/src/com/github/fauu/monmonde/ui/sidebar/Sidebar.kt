package com.github.fauu.monmonde.ui.sidebar

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.graphics.Color
import com.github.fauu.monmonde.Message
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.github.fauu.monmonde.Screen
import com.github.fauu.monmonde.ScreenType
import com.github.fauu.monmonde.screens.battle.BattleScreen
import com.github.fauu.monmonde.screens.exploration.ExplorationScreen
import com.github.fauu.monmonde.screens.travel.TravelScreen
import com.github.fauu.monmonde.ui.sidebar.exploration.VisibleMonList
import com.github.fauu.monmonde.ui.sidebar.travel.LocationActionMenu

class Sidebar(uiSkin: Skin): com.badlogic.gdx.Screen, Telegraph {
	private val uiSkin = uiSkin
  private val messenger = MessageManager.getInstance()
	private val camera = OrthographicCamera()
	private val viewport = ScalingViewport(Scaling.none, 352f, 800f, camera)
	private val stage = Stage(viewport)
  private val layout = Table()

	private val travelScreenLayout = VerticalGroup()
	private val explorationScreenLayout = VerticalGroup()
  private val battleScreenLayout = VerticalGroup()

  private val playerMonParty = PlayerMonParty(uiSkin)
  private val clock = Clock(uiSkin)

	init {
    messenger.addListeners(this, Message.ACTIVE_SCREEN_CHANGED)

    viewport.setScreenPosition(928, 0)

    setupTravelScreenLayout()
    setupExplorationScreenLayout()

    stage.addActor(layout)
	}

  private fun buildLayout(activeScreen: Screen) {
    layout.clearChildren()

    layout.setFillParent(true)
    layout.align(Align.topLeft)
    layout.pad(2f, 15f, 5f, 8f) // CCW

    when (activeScreen) {
      is TravelScreen -> {
        layout.add(travelScreenLayout).top().left().expand()
      }
      is ExplorationScreen -> {
        layout.add(explorationScreenLayout).top().left().expand()
      }
      is BattleScreen -> {
        layout.add(battleScreenLayout).top().left().expand()
      }
    }

    layout.row().spaceBottom(130f)
    layout.add(playerMonParty).bottom().fillX()

    layout.row()
    layout.add(clock).bottom().fillX()
  }

  private fun setupExplorationScreenLayout() {
    explorationScreenLayout.align(Align.topLeft)
    explorationScreenLayout.space(-6f)

    val monCycleLabel = Label("Press UP or DOWN to cycle through mons", uiSkin,
        "small-font", Color.GRAY)
    explorationScreenLayout.addActor(monCycleLabel)

    val monActionLabel = Label("Press RETURN to engage selected mon", uiSkin,
        "small-font", Color.GRAY)
    explorationScreenLayout.addActor(monActionLabel)

    val visibleMonList = VisibleMonList(uiSkin)
    visibleMonList.padTop(7f)
    visibleMonList.space(-8f)
    explorationScreenLayout.addActor(visibleMonList)
  }
	
	private fun setupTravelScreenLayout() {
    travelScreenLayout.align(Align.topLeft)
    travelScreenLayout.space(-3f)

    travelScreenLayout.addActor(LocationActionMenu(uiSkin))
    travelScreenLayout.align(Align.topLeft)
	}

	override fun handleMessage(telegram: Telegram?): Boolean {
		if (telegram == null) return true
		
		when (telegram.message) {
			Message.ACTIVE_SCREEN_CHANGED -> {
        buildLayout(telegram.extraInfo as Screen)
			}
		}
		
		return true
	}

	override fun pause() {
		throw UnsupportedOperationException()
	}

	override fun show() {
		throw UnsupportedOperationException()
	}

	override fun resize(width: Int, height: Int) {
		viewport.update(width, height)
	}

	override fun hide() {
		throw UnsupportedOperationException()
	}

	override fun render(delta: Float) {
    viewport.apply()
    stage.act(delta)
    stage.draw()
	}

	override fun resume() {
		throw UnsupportedOperationException()
	}

	override fun dispose() = stage.dispose()
}