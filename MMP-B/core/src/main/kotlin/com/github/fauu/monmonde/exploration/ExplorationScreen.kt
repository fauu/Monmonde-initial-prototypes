package com.github.fauu.monmonde.exploration

import com.artemis.World
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.GridPoint2
import com.github.fauu.monmonde.exploration.components.*
import com.github.fauu.monmonde.shared.Assets
import com.github.fauu.monmonde.shared.components.IsPlayerControlled
import com.github.fauu.monmonde.shared.components.HasSprite
import com.github.fauu.monmonde.exploration.types.AreaMap

class ExplorationScreen(private val camera: OrthographicCamera,
                        private val batch: SpriteBatch):
    ScreenAdapter() {
  private val messenger = MessageManager.getInstance()

  // TODO: Check out cached renderer
  private lateinit var mapRenderer: OrthogonalTiledMapRenderer

  var enabled = true

  lateinit var world: World

  fun init() {
    val cHasAreaMap = HasAreaMap()
    // TODO: Move initialization into a system?
    cHasAreaMap.areaMap = AreaMap()
    world.createEntity()
        .edit()
        .add(cHasAreaMap)
    mapRenderer = OrthogonalTiledMapRenderer(cHasAreaMap.areaMap, 1/16f, batch)
    mapRenderer

    camera.setToOrtho(false, 29f, 25f)
    camera.update()

    val atlas = Assets.TEXTURE_ATLAS

    val playerSprite = atlas.createSprite("exploration/sprites/player")
    playerSprite.setSize(1f, 1f)
    var cHasSprite = HasSprite()
    cHasSprite.sprite = playerSprite
    var cHasPosition = HasPosition()
    cHasPosition.position = GridPoint2(5, 5)
    world.createEntity()
        .edit()
        .add(IsPlayerControlled())
        .add(cHasSprite)
        .add(cHasPosition)
        .add(IsCameraTarget())

    val monSprite = atlas.createSprite("exploration/sprites/16")
    monSprite.setSize(1f, 1f)
    cHasSprite = HasSprite()
    cHasSprite.sprite = monSprite
    cHasPosition = HasPosition()
    cHasPosition.position = GridPoint2(10, 10)
    world.createEntity()
        .edit()
        .add(IsWildMon())
        .add(cHasSprite)
        .add(cHasPosition)

  }

  override fun render(delta: Float) {
    if (!enabled) return

    mapRenderer.setView(camera)
    mapRenderer.render()
  }
}

