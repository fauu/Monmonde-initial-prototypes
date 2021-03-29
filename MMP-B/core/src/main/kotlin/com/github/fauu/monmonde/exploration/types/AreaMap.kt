package com.github.fauu.monmonde.exploration.types

import com.badlogic.gdx.maps.tiled.*
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.GridPoint2
import com.github.fauu.monmonde.shared.Assets
import java.util.*

class AreaMap: TiledMap() {
  private val tileset = TiledMapTileSet()
  private val concreteLayers = mutableListOf<TiledMapTileLayer>()

  val visibleTilesCoordinates = mutableListOf<GridPoint2>()

  init {
    val random = Random()

    val atlas = Assets.TEXTURE_ATLAS

    var tile: TiledMapTile
    tile = StaticTiledMapTile(atlas.findRegion("exploration/tiles/internal/obscuration"))
    tileset.putTile(0, tile)
    tile = StaticTiledMapTile(atlas.findRegion("exploration/tiles/grass", 0))
    tile.properties.put("speedCoefficient", 1f)
    tileset.putTile(1, tile)
    tile = StaticTiledMapTile(atlas.findRegion("exploration/tiles/tree", 0))
    tile.properties.put("speedCoefficient", 0f)
    tile.properties.put("obscuresView", true)
    tileset.putTile(2, tile)

    run {
      val layer: TiledMapTileLayer
      layer = TiledMapTileLayer(1000, 1000, 16, 16)
      layer.name = "ground"
      for (i in 0..999) {
        for (j in 0..999) {
          val cell = TiledMapTileLayer.Cell()
          cell.tile = tileset.getTile(1)
          layer.setCell(i, j, cell)
        }
      }
      concreteLayers.add(layer)
      layers.add(layer)
    }

    run {
      val layer = TiledMapTileLayer(1000, 1000, 16, 16)
      layer.name = "objects"
      for (i in 0..999) {
        for (j in 0..999) {
          if (random.nextInt(10) != 0) continue

          val cell = TiledMapTileLayer.Cell()
          cell.tile = tileset.getTile(2)
          layer.setCell(i, j, cell)
        }
      }
      concreteLayers.add(layer)
      layers.add(layer)
    }

    run {
      val layer = TiledMapTileLayer(1000, 1000, 16, 16)
      layer.name = "los"
      for (i in 0..999) {
        for (j in 0..999) {
          val cell = TiledMapTileLayer.Cell()
          layer.setCell(i, j, cell)
        }
      }
      layer.opacity = .85f
      layers.add(layer)
    }
  }

  fun speedAt(location: GridPoint2): Float =
    concreteLayers.map {
      it.getCell(location.x, location.y)
          ?.tile
          ?.properties
          ?.get("speedCoefficient") as? Float
    }.fold(1f) { acc, x -> x?.times(acc) ?: acc }

  fun hasViewObstacleAt(x: Int, y: Int): Boolean {
    val objectLayer = layers["objects"] as TiledMapTileLayer
    val cell = objectLayer.getCell(x, y)

    return cell?.tile?.properties?.get("obscuresView") as? Boolean ?: false
  }

  fun setTileObscured(at: GridPoint2, obscured: Boolean = true) {
    val losLayer = layers["los"] as TiledMapTileLayer
    val cell = losLayer.getCell(at.x, at.y)

    cell?.tile = if (obscured) tileset.getTile(0) else null
  }

  fun isTileAtVisibleFrom(at: GridPoint2, from: GridPoint2): Boolean {
    if (from.equals(at)) return true

    var t: Int
    var x: Int
    var y: Int
    val absDeltaX: Int
    val absDeltaY: Int
    val signX: Int
    val signY: Int
    val deltaX: Int
    val deltaY: Int

    fun sgn(x: Int) = if (x < 0) -1 else 1

    deltaX = from.x - at.x
    deltaY = from.y - at.y

    absDeltaX = Math.abs(deltaX)
    absDeltaY = Math.abs(deltaY)

    signX = sgn(deltaX)
    signY = sgn(deltaY)

    x = at.x;
    y = at.y;

//    if ((at.x - from.x) * (at.x - from.x) + (at.y - from.y) * (at.y - from.y) > 100) return false

    if (absDeltaX > absDeltaY) {
      t = absDeltaY * 2 - absDeltaX
      do {
        if (t >= 0) {
          y += signY
          t -= absDeltaX * 2
        }

        x += signX
        t += absDeltaY * 2

        if (x == from.x && y == from.y) {
          return true
        }
      }
      while (!hasViewObstacleAt(x, y))

      return false
    }
    else
    {
      t = absDeltaX * 2 - absDeltaY
      do {
        if (t >= 0) {
          x += signX
          t -= absDeltaY * 2
        }

        y += signY
        t += absDeltaX * 2

        if (x == from.x && y == from.y) {
          return true
        }
      }
      while (!hasViewObstacleAt(x, y))

      return false
    }
  }
}