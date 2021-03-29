package com.github.fauu.monmonde.persistence

import com.github.fauu.monmonde.screens.exploration.TileType

class PersistedTile {
	var x: Int = 0
	var y: Int = 0
  var type: TileType? = null
  var layer: Int = 0
}