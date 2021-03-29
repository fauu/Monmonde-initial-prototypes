package com.github.fauu.monmonde

import aurelienribon.tweenengine.Tween
import aurelienribon.tweenengine.TweenManager
import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.fauu.monmonde.battle.BattleScreen
import com.github.fauu.monmonde.battle.systems.*
import com.github.fauu.monmonde.exploration.ExplorationScreen
import com.github.fauu.monmonde.exploration.systems.*
import com.github.fauu.monmonde.shared.Assets
import com.github.fauu.monmonde.shared.systems.*
import com.github.fauu.monmonde.ui.Ui
import com.github.fauu.monmonde.utils.accessors.FadeScreenSystemAccessor
import com.github.fauu.monmonde.utils.accessors.OrthographicCameraAccessor
import se.feomedia.orion.system.OperationSystem

class Monmonde : ApplicationAdapter() {
  private lateinit var stage: Stage
  private val screens = mutableListOf<Screen>()
  private lateinit var world: World
  private lateinit var batch: SpriteBatch
  private val camera = OrthographicCamera()

  private lateinit var shader: ShaderProgram
  private lateinit var transition: Texture

  private val tweenManager = TweenManager()

  init {
    Tween.registerAccessor(OrthographicCamera::class.java,
        OrthographicCameraAccessor())
    Tween.registerAccessor(FadeScreen::class.java,
        FadeScreenSystemAccessor())
  }

  override fun create() {
    shader = ShaderPrograms.create("default")

    batch = SpriteBatch()
    batch.shader = shader

    stage = Stage(ScreenViewport(), batch)
    val ui = Ui()
    ui.setFillParent(true)
    stage.addActor(Ui())


    val battleScreen = BattleScreen(stage, camera)
    val explorationScreen = ExplorationScreen(camera, batch)
    val renderBatchingSystem = RenderBatchingSystem(batch)

    val battleSystems = listOf(
        HandleBattleInput(),
        OrderAiBattleMove(),
        StartOrderedBattleMoves(),
        PerformBattleMoves(),
        EffectuateBattleMoveConsequences(),
        PerformRest(),
        ColorizeSprites(),
        AnimateSpriteScales(),
        AnimateSpriteFrames()
    )

    val explorationSystems = listOf(
        HandleExplorationInput(),
        UpdateVision(),
        UpdateWildMonsVisibility(),
        MoveAreaEntitiesAndMaybeInitBattle(),
        SyncPositions(),
        LockCamera()
    )

    val worldConfiguration = WorldConfiguration()
    with (worldConfiguration) {
      register(EntityLinkManager())

      register(camera)
      register(tweenManager)
      register(shader)
      register("battleSystems", battleSystems)
      register("explorationSystems", explorationSystems)

      register(battleScreen)
      register(explorationScreen)

      battleSystems.forEach { setSystem(it) }
      explorationSystems.forEach { setSystem(it) }

      setSystem(OperationSystem())
      setSystem(HandleGameInput())

      setSystem(AnimateSpritePositions())

      setSystem(renderBatchingSystem)
      setSystem(RenderSprites(renderBatchingSystem))
      setSystem(FadeScreen())
    }

    world = World(worldConfiguration)

    battleScreen.world = world
    explorationScreen.world = world

    explorationScreen.init()
    battleSystems.forEach { it.isEnabled = false }
    explorationSystems.forEach { it.isEnabled = true }

    screens.add(battleScreen)
    screens.add(explorationScreen)
  }

  override fun render() {
    val delta = Gdx.graphics.deltaTime

    tweenManager.update(delta)
    camera.update()

    Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

    Gdx.gl.glViewport(0, 0, 928, 800)

    screens.forEach { it.render(delta) }

    batch.projectionMatrix = camera.combined
    world.delta = delta
    world.process()

    Gdx.gl.glViewport(0, 0, 1280, 800)

    stage.act(delta)
    stage.draw()

    DevConsole.render()
  }

  override fun dispose() {
    stage.dispose()
    screens.forEach { it.dispose() }
    DevConsole.dispose()
    Assets.dispose()
  }
}
