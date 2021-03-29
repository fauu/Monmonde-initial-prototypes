package com.github.fauu.monmonde.exploration.systems

import com.artemis.Aspect
import com.artemis.AspectSubscriptionManager
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.systems.IteratingSystem
import com.github.fauu.monmonde.exploration.components.HasAreaMap
import com.github.fauu.monmonde.exploration.components.HasPosition
import com.github.fauu.monmonde.shared.components.IsHidden
import com.github.fauu.monmonde.exploration.components.IsWildMon
import com.github.fauu.monmonde.exploration.types.AreaMap

class UpdateWildMonsVisibility():
    IteratingSystem(
      Aspect.all(IsWildMon::class.java, HasPosition::class.java)
    ) {
  private lateinit var aspectSubscriptionManager: AspectSubscriptionManager
  private lateinit var areaSubscription: EntitySubscription

  private lateinit var mHasAreaMap: ComponentMapper<HasAreaMap>
  private lateinit var mHasPosition: ComponentMapper<HasPosition>
  private lateinit var mIsHidden: ComponentMapper<IsHidden>

  private lateinit var areaMap: AreaMap

  override fun initialize() {
    aspectSubscriptionManager = world.aspectSubscriptionManager
    areaSubscription =
        aspectSubscriptionManager.get(Aspect.all(HasAreaMap::class.java))
    super.initialize()
  }

  override fun begin() {
    areaMap = mHasAreaMap[areaSubscription.entities[0]].areaMap
    super.begin()
  }

  override fun process(e: Int) {
    val position = mHasPosition[e].position
    val visible = position in areaMap.visibleTilesCoordinates

    mIsHidden.set(e, !visible)
  }
}

