package com.github.fauu.monmonde.shared

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Disposable

object Assets: Disposable {
  lateinit var TEXTURE_ATLAS: TextureAtlas
  val FONT_GENERATORS = mapOf(
      "regular" to FreeTypeFontGenerator(Gdx.files.internal("fonts/regular.ttf")),
      "medium" to FreeTypeFontGenerator(Gdx.files.internal("fonts/medium.ttf")),
      "bold" to FreeTypeFontGenerator(Gdx.files.internal("fonts/bold.ttf"))
  )

  private val manager = AssetManager()

  init {
    val atlasDescriptor
        = AssetDescriptor<TextureAtlas>("atlas.atlas", TextureAtlas::class.java)
    manager.load(atlasDescriptor)
    manager.finishLoading()
    TEXTURE_ATLAS = manager.get(atlasDescriptor)
  }

  override fun dispose() {
    for ((k, v) in FONT_GENERATORS) v.dispose()
  }
}