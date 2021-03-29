package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
import com.github.fauu.monmonde.utils.GridBounds

class ExplorationCamera: OrthographicCamera() {
  private val VIEW_WORLD_SIZE = GridPoint2(29, 25)

  var followTarget: Actor? = null

  val worldPosition: GridPoint2
    get() {
      return GridPoint2((position.x / 16f).toInt(), (position.y / 16f).toInt())
    }

  val worldBounds: GridBounds
    get() {
      val distancesToEdge =
          GridPoint2(VIEW_WORLD_SIZE.x / 2, VIEW_WORLD_SIZE.y / 2)

      val start = worldPosition.cpy().sub(distancesToEdge)
      val end = worldPosition.cpy().add(distancesToEdge)

      return GridBounds(start, end)
    }

  init {
    setToOrtho(false)
    position.set(0f, 0f, 0f)
    zoom = .5f;
    update()
  }

  fun update(delta: Float) {
    followTarget?.let { position.set(it.x, it.y, 0f) }

    super.update()
  }
}

