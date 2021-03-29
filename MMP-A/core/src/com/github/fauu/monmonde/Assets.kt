package com.github.fauu.monmonde

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.github.fauu.monmonde.screens.exploration.Tileset
import com.github.fauu.monmonde.screens.exploration.TilesetLoader
import com.github.fauu.monmonde.screens.travel.RegionMap
import com.github.fauu.monmonde.screens.travel.RegionMapLoader

object Assets: Disposable {
  val SKIN =
      AssetDescriptor<Skin>("uiskin.json", Skin::class.java)
  val UI_SPRITES =
      AssetDescriptor<Texture>("ui-sprites.png", Texture::class.java)
  // Travel
  val REGION_MAP =
      AssetDescriptor<RegionMap>("regions/demo/region.json", RegionMap::class.java)
  // Exploration
  val EXPLORATION_SPRITES =
      AssetDescriptor<Texture>("sprites.png", Texture::class.java)
  val TILESET =
      AssetDescriptor<Tileset>("tileset.json", Tileset::class.java)
  val EXPLORATION_VIGNETTE =
      AssetDescriptor<Texture>("vignette.png", Texture::class.java)
  // Battle
  val BATTLE_SPRITES =
      AssetDescriptor<Texture>("battle-sprites.png", Texture::class.java)
  val BATTLE_BACKGROUNDS =
      arrayOf(AssetDescriptor<Texture>("battle-backgrounds/meadow.png", Texture::class.java))

  private val manager = AssetManager()

  init {
    val resolver = InternalFileHandleResolver()

    with (manager) {
      setLoader(RegionMap::class.java, RegionMapLoader(resolver))
      setLoader(Tileset::class.java, TilesetLoader(resolver))
    }
  }

  fun load(descriptors: List<AssetDescriptor<out Any>>) =
    with (manager) {
      descriptors.forEach { load(it) }
      finishLoading()
    }

  fun <T> get(descriptor: AssetDescriptor<out T>): T = manager.get(descriptor)

  override fun dispose() = manager.dispose()
}