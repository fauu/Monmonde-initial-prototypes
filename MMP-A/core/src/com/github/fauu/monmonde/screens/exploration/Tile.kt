package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.graphics.g2d.TextureRegion

data class Tile(val textureRegion: TextureRegion,
                val type: TileType,
                val layer: Int)
