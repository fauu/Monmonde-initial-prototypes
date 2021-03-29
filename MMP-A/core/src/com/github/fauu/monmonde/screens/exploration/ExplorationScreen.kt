package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.fauu.monmonde.*
import com.github.fauu.monmonde.camera.MoveAction
import com.github.fauu.monmonde.camera.ZoomAction
import com.github.fauu.monmonde.screens.battle.BattleScreen

class ExplorationScreen(batch: SpriteBatch) : Screen(batch) {
  companion object {
    val ASSET_DEPENDENCIES = listOf(Assets.EXPLORATION_SPRITES, Assets.TILESET, Assets.EXPLORATION_VIGNETTE)
  }

	private lateinit var camera: ExplorationCamera
	private lateinit var mapRenderer: OrthogonalTiledMapRenderer
  private lateinit var area: Area
  private lateinit var areaPlayer: AreaPlayer

  private val vignetteCamera = OrthographicCamera()
	private lateinit var vignette: Texture

  private val mons = Array<AreaMon>()

  override fun prepare() {
    if (ready) return

    Assets.load(ASSET_DEPENDENCIES)

    messenger.addListeners(this, Message.VISIBLE_MON_SELECTED,
                                 Message.ENCOUNTER_INITIATED,
                                 Message.AREA_PLAYER_MOVED)

		area = Area(Assets.get(Assets.TILESET))
		
    camera = ExplorationCamera()
    viewport = ScalingViewport(Scaling.none, WIDTH, HEIGHT, camera)

		mapRenderer = OrthogonalTiledMapRenderer(area.map);

    val spritesTexture = Assets.get(Assets.EXPLORATION_SPRITES)

		areaPlayer = AreaPlayer(area, TextureRegion(spritesTexture, 0, 0, 16, 16))
    areaPlayer.moveTo(30, 30)

    vignette = Assets.get(Assets.EXPLORATION_VIGNETTE)
    vignetteCamera.setToOrtho(false)

		stage = Stage(viewport, batch)
		stage.addActor(areaPlayer)

    var mon = AreaMon(Mon(MonKind.PIDGEY), GridPoint2(33, 40), spritesTexture)
    mons.add(mon)
    stage.addActor(mon)
    mon = AreaMon(Mon(MonKind.PIDGEY), GridPoint2(39, 42), spritesTexture)
    mons.add(mon)
    stage.addActor(mon)

    camera.followTarget = areaPlayer
    camera.update(Gdx.graphics.deltaTime)

    ready = true
	}

  override fun activate() {
    camera.position.x = 30 * 16f
    camera.position.y = 30 * 16f
    updateLos()
    updateMonVisibility()
  }

  override fun handleMessage(msg: Telegram?): Boolean {
    if (msg == null) return true

    when (msg.message) {
      Message.VISIBLE_MON_SELECTED -> {
        area.unhighlightTile()

        if (msg.extraInfo != null) {
          val mon = msg.extraInfo as AreaMon

          area.highlightTileAt(mon.worldPosition.x, mon.worldPosition.y)
        }
      }
      Message.ENCOUNTER_INITIATED -> {
        val mon = msg.extraInfo as AreaMon

        area.unhighlightTile()
        camera.followTarget = null
        stage.addAction(MoveAction(camera, Vector2(mon.x + 8f, mon.y + 8f), 1f, Interpolation.exp5In))
        stage.addAction(ZoomAction(camera, 0.05f, 1f, Interpolation.exp5In))
        // TODO: Add easing

        messenger.dispatchMessage(Message.ACTIVE_SCREEN_CHANGE_REQUESTED,
            BattleScreen::class.java)
      }
      Message.AREA_PLAYER_MOVED -> {
        updateLos()
        updateMonVisibility()
      }
    }

    return true
  }

  private fun updateLos() {
    val bounds = camera.worldBounds
    bounds.start.x = MathUtils.clamp(bounds.start.x, 0, 10000)
    bounds.start.y = MathUtils.clamp(bounds.start.y, 0, 10000)

    for (x in bounds.start.x..bounds.end.x) {
      for (y in bounds.start.y..bounds.end.y) {
        val visible =
            area.isTileAtVisibleFrom(GridPoint2(x, y), areaPlayer.worldPosition)

        area.losShowAt(x, y, visible);
      }
    }
  }

  private fun updateMonVisibility() {
    mons.forEach { it.isVisible = area.isTileFowed(it.worldPosition) }

    messenger.dispatchMessage(Message.VISIBLE_MONS_CHANGED,
        mons.filter { it.isVisible })
  }

  override fun processInput(delta: Float) = Unit

	override fun render(delta: Float) {
    camera.update(delta)

    // TODO: Implement resize() and move there?
    viewport.setScreenPosition(0, 0)
    viewport.apply()

    mapRenderer.setView(camera)
    mapRenderer.render()

    stage.act(delta)
    stage.draw()

    Gdx.gl.glViewport(0, 0, Gdx.graphics.width* 2, Gdx.graphics.height * 2)
    with (batch) {
      projectionMatrix = vignetteCamera.combined
      vignetteCamera.update()

      begin()
      draw(vignette, 0f, 0f)
      end()
    }
	}

	override fun dispose() {
    mapRenderer.dispose()

    super.dispose()
  }
}