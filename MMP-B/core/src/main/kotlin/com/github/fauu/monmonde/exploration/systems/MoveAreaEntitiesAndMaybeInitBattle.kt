package com.github.fauu.monmonde.exploration.systems

import aurelienribon.tweenengine.*
import com.artemis.*
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.github.fauu.monmonde.utils.extensions.toVector2
import com.badlogic.gdx.utils.Timer
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.battle.BattleScreen
import com.github.fauu.monmonde.shared.components.HasSprite
import com.github.fauu.monmonde.battle.components.HasSpritePositionAnimation
import com.github.fauu.monmonde.battle.components.WillMove
import com.github.fauu.monmonde.exploration.ExplorationScreen
import com.github.fauu.monmonde.exploration.components.*
import com.github.fauu.monmonde.shared.components.IsHidden
import com.github.fauu.monmonde.shared.systems.FadeScreen
import com.github.fauu.monmonde.utils.accessors.FadeScreenSystemAccessor
import com.github.fauu.monmonde.utils.accessors.OrthographicCameraAccessor
import com.github.fauu.monmonde.utils.extensions.isOnAnyAxis

// TODO: Two concerns, try to separate them
class MoveAreaEntitiesAndMaybeInitBattle :
    IteratingSystem(
        Aspect.all(WillMove::class.java, HasSprite::class.java)
              .exclude(IsMoving::class.java)
    ) {
  @Wire private lateinit var camera: OrthographicCamera
  @Wire private lateinit var tweenManager: TweenManager
  @Wire private lateinit var battleScreen: BattleScreen
  @Wire private lateinit var explorationScreen: ExplorationScreen
  @Wire(name = "battleSystems")
  private lateinit var battleSystems: List<BaseSystem>
  @Wire(name = "explorationSystems")
  private lateinit var explorationSystems: List<BaseSystem>

  private lateinit var sLockCamera: LockCamera
  private lateinit var sFadeScreen: FadeScreen

  private lateinit var aspectSubscriptionManager: AspectSubscriptionManager
  private lateinit var areaSubscription: EntitySubscription
  private lateinit var visibleMonsSubscription: EntitySubscription

  private lateinit var mWillMove: ComponentMapper<WillMove>
  private lateinit var mIsMoving: ComponentMapper<IsMoving>
  private lateinit var mHasSpritePositionAnimation:
      ComponentMapper<HasSpritePositionAnimation>
  private lateinit var mHasPosition: ComponentMapper<HasPosition>
  private lateinit var mHasAreaMap: ComponentMapper<HasAreaMap>

  override fun initialize() {
    aspectSubscriptionManager = world.aspectSubscriptionManager
    areaSubscription =
        aspectSubscriptionManager.get(Aspect.all(HasAreaMap::class.java))
    visibleMonsSubscription =
        aspectSubscriptionManager.get(
            Aspect.all(IsWildMon::class.java).exclude(IsHidden::class.java))
    super.initialize()
  }

  override fun process(e: Int) {
    val currentPosition = mHasPosition[e].position
    val moveVector = mWillMove[e].vector
    val areaMap = mHasAreaMap[areaSubscription.entities[0]].areaMap

    mWillMove.remove(e)

    val targetPosition = currentPosition.cpy().add(moveVector)
    val targetTileSpeedCoefficient = areaMap.speedAt(targetPosition)

    val visibleMonIds = visibleMonsSubscription.entities
    for (i in 0..visibleMonIds.size() - 1) {
      val position = mHasPosition[visibleMonIds[i]].position

      if (targetPosition == position) {
        sLockCamera.isEnabled = false
        val cameraXyTween =
            Tween.to(camera, OrthographicCameraAccessor.TYPE_XY, 2f)
              .target(targetPosition.x.toFloat() + .5f,
                      targetPosition.y.toFloat() + .5f)
              .ease(TweenEquations.easeInCubic)
        val cameraZoomInTween =
            Tween.to(camera, OrthographicCameraAccessor.TYPE_ZOOM, 2f)
                .target(.00001f)
                .ease(TweenEquations.easeInCubic)
        val screenFadeOutTween =
            Tween.to(sFadeScreen, FadeScreenSystemAccessor.TYPE_FADE_LEVEL, 2f)
                .target(1f)
                .ease(TweenEquations.easeInCubic)
        val screenFadeInTween =
            Tween.to(sFadeScreen, FadeScreenSystemAccessor.TYPE_FADE_LEVEL, 2f)
                .target(0f)
                .ease(TweenEquations.easeInCubic)

        sFadeScreen.setColor(Color.WHITE)

        val fadeOutTimeline =
            Timeline.createParallel()
                .push(cameraXyTween)
                .push(cameraZoomInTween)
                .push(screenFadeOutTween)

        val fadeInTimeline =
            Timeline.createParallel()
                .push(screenFadeInTween)

        Timeline.createSequence()
            .push(fadeOutTimeline)
            .delay(.5f)
            .push(fadeInTimeline)
            .start(tweenManager)

        Timer.schedule(object: Timer.Task() {
          override fun run() {
            battleScreen.init()
            battleSystems.forEach { it.isEnabled = true }

            explorationScreen.enabled = false
          }
        }, 3.2f)

        explorationSystems.forEach { it.isEnabled = false }

        return
      }
    }

    if (targetTileSpeedCoefficient == 0f) return

    val directionCoefficient = if (!moveVector.isOnAnyAxis) 0.707f else 1f

    val moveDuration = .3f / (targetTileSpeedCoefficient * directionCoefficient)

    mIsMoving.set(e, true)
    mHasPosition[e].position.set(targetPosition)

    // TODO: Replace with UTE
    val cSpritePositionAnimation = mHasSpritePositionAnimation.create(e)
    cSpritePositionAnimation.amount = moveVector.toVector2()
    cSpritePositionAnimation.duration = moveDuration

    // Meh
    Timer.schedule(object: Timer.Task() {
      override fun run() {
        mIsMoving.set(e, false)
      }
    }, moveDuration)
  }
}