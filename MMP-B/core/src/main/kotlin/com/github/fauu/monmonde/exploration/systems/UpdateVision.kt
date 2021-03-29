package com.github.fauu.monmonde.exploration.systems

import com.artemis.*
import com.artemis.annotations.Wire
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.GridPoint2
import com.github.fauu.monmonde.exploration.components.HasAreaMap
import com.github.fauu.monmonde.exploration.components.HasPosition
import com.github.fauu.monmonde.shared.components.IsPlayerControlled

class UpdateVision(): BaseSystem() {
  private lateinit var aspectSubscriptionManager: AspectSubscriptionManager
  private lateinit var playerSubscription: EntitySubscription
  private lateinit var areaSubscription: EntitySubscription

  @Wire
  private lateinit var camera: OrthographicCamera

  private lateinit var mHasAreaMap: ComponentMapper<HasAreaMap>
  private lateinit var mHasPosition: ComponentMapper<HasPosition>

  override fun initialize() {
    aspectSubscriptionManager = world.aspectSubscriptionManager
    playerSubscription =
        aspectSubscriptionManager.get(
            Aspect.all(IsPlayerControlled::class.java))
    areaSubscription =
        aspectSubscriptionManager.get(Aspect.all(HasAreaMap::class.java))
    super.initialize()
  }

  override fun processSystem() {
    val areaMap = mHasAreaMap[areaSubscription.entities[0]].areaMap
    val playerPosition = mHasPosition[playerSubscription.entities[0]].position

    // TODO: Extend camera?
    val cameraGridPosition =
        GridPoint2((camera.position.x).toInt(), (camera.position.y).toInt())
    val cameraDistancesToEdge = GridPoint2(32 / 2, 28 / 2)
    val cameraBoundsStart = cameraGridPosition.cpy().sub(cameraDistancesToEdge)
    val cameraBoundsEnd = cameraGridPosition.cpy().add(cameraDistancesToEdge)

    areaMap.visibleTilesCoordinates.clear()
    for (x in cameraBoundsStart.x..cameraBoundsEnd.x) {
      for (y in cameraBoundsStart.y..cameraBoundsEnd.y) {
        val gridPoint = GridPoint2(x, y)

        val visible = areaMap.isTileAtVisibleFrom(gridPoint, playerPosition)

        if (visible) areaMap.visibleTilesCoordinates.add(gridPoint)

        areaMap.setTileObscured(GridPoint2(x, y), !visible)
      }
    }
  }
}
