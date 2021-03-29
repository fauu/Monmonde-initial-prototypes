package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import java.util.Random
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.GridPoint2

class Area(tileset: Tileset) {
	val map = TiledMap()

  private var losMapTile: TiledMapTile
  private var highlightMapTile: TiledMapTile

  private var highlightedTileX: Int? = null
  private var highlightedTileY: Int? = null

  // TODO: Use TiledMapTileSet
	init {
		val random = Random()

    val sourceTiles = tileset.tiles
    val groundTiles = sourceTiles.filter { it.layer == 0 }
    val numGroundTiles = groundTiles.size
    val upperTiles = sourceTiles.filter { it.layer == 1 }
    val numUpperTiles = upperTiles.size
    val losTile = sourceTiles.first { it.layer == -1 }
    val highlightTile = sourceTiles.first { it.layer == -2 }

    var layer = TiledMapTileLayer(100, 100, 16, 16)
		for (i in 0..99) {
			for (j in 0..99) {
				val cell = TiledMapTileLayer.Cell()

        val sourceTile = groundTiles[random.nextInt(numGroundTiles)]
				val tile = StaticTiledMapTile(sourceTile.textureRegion)
        tile.properties.put("speed", sourceTile.type.speed)
        tile.blendMode = TiledMapTile.BlendMode.NONE

        cell.tile = tile

				layer.setCell(i, j, cell)
			}
		}
		map.layers.add(layer)

    layer = TiledMapTileLayer(100, 100, 16, 16)
    for (i in 0..99) {
      for (j in 0..99) {
        if (random.nextInt(10) != 0) continue

        val cell = TiledMapTileLayer.Cell()

        val sourceTile = upperTiles[random.nextInt(numUpperTiles)]
        val tile = StaticTiledMapTile(sourceTile.textureRegion)
        tile.properties.put("speed", sourceTile.type.speed)
        tile.blendMode = TiledMapTile.BlendMode.NONE

        cell.tile = tile

        layer.setCell(i, j, cell)
      }
    }
    map.layers.add(layer)

    losMapTile = StaticTiledMapTile(losTile.textureRegion)
    losMapTile.blendMode = TiledMapTile.BlendMode.NONE

    // LoS może być rysowane na TiledMapImageLayer?
    layer = TiledMapTileLayer(100, 100, 16, 16)
    for (i in 0..99) {
      for (j in 0..99) {
        val cell = TiledMapTileLayer.Cell() // TODO: Share?

        cell.tile = losMapTile

        layer.setCell(i, j, cell)
      }
    }
    layer.opacity = 0.85f
    map.layers.add(layer)

    // Highlight layer
    layer = TiledMapTileLayer(100, 100, 16, 16)
    highlightMapTile = StaticTiledMapTile(highlightTile.textureRegion)
    highlightMapTile.blendMode = TiledMapTile.BlendMode.NONE
    for (i in 0..99) {
      for (j in 0..99) {
        val cell = TiledMapTileLayer.Cell() // TODO: Share?
        layer.setCell(i, j, cell)
      }
    }
    layer.opacity = 0.6f
    map.layers.add(layer)
	}

  fun speedAt(point: GridPoint2): Float {
    return map.layers.map {
      (it as TiledMapTileLayer).getCell(point.x, point.y)
                               ?.tile
                               ?.properties
                               ?.get("speed") as? Float
    }.fold(1f) { acc, x -> x?.times(acc) ?: acc}
  }

  fun hasObstacleAt(x: Int, y: Int): Boolean {
    return map.layers.map {
      (it as TiledMapTileLayer).getCell(x, y)
          ?.tile
          ?.properties
          ?.get("speed") as? Float
    }.fold(false) { acc, x -> acc || x == 0f }
  }

  fun losShowAt(x: Int, y: Int, show: Boolean) {
    val losLayer = map.layers.get(2) as TiledMapTileLayer

    losLayer.getCell(x, y).tile = if (show) null else losMapTile
  }

  private fun highlightTileAt(x: Int, y: Int, highlight: Boolean) {
    val highlightLayer = map.layers.get(3) as TiledMapTileLayer

    highlightLayer.getCell(x, y).tile =
        if (highlight) highlightMapTile else null

    if (highlight) {
      highlightedTileX = x
      highlightedTileY = y
    } else {
      highlightedTileX = null
      highlightedTileY = null
    }
  }


  fun highlightTileAt(x: Int, y: Int) {
    highlightTileAt(x, y, true)
  }

  fun unhighlightTile() {
    if (highlightedTileX != null) {
      highlightTileAt(highlightedTileX!!, highlightedTileY!!, false)
    }
  }

  fun isTileFowed(coords: GridPoint2): Boolean {
    val losLayer = map.layers.get(2) as TiledMapTileLayer

    return losLayer.getCell(coords.x, coords.y).tile == null
  }

  // TODO: Move to Player? seesTileAt() or sth
  // http://www.roguebasin.com/index.php?title=Simple_Line_of_Sight
  fun isTileAtVisibleFrom(at: GridPoint2, from: GridPoint2): Boolean {
    if (from.equals(at)) return true

    var t: Int
    var x: Int
    var y: Int
    var absDeltaX: Int
    var absDeltaY: Int
    var signX: Int
    var signY: Int
    var deltaX: Int
    var deltaY: Int

    fun sgn(x: Int) = if (x < 0) -1 else 1

    deltaX = from.x - at.x
    deltaY = from.y - at.y

    absDeltaX = Math.abs(deltaX)
    absDeltaY = Math.abs(deltaY)

    signX = sgn(deltaX)
    signY = sgn(deltaY)

    x = at.x;
    y = at.y;

    if ((at.x - from.x) * (at.x - from.x) + (at.y - from.y) * (at.y - from.y) > 100) return false

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
      while (!hasObstacleAt(x, y))

      return false
    }
    else
    {
      t = absDeltaX * 2 - absDeltaY;
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
      while (!hasObstacleAt(x, y))

      return false
    }
  }
}