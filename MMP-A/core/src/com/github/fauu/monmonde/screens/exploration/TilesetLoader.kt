package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json

import com.github.fauu.monmonde.persistence.PersistedTileset

class TilesetLoader(resolver: FileHandleResolver)
    : SynchronousAssetLoader<Tileset, TilesetLoader.TilesetParameter>(resolver) {
	override fun load(assetManager: AssetManager, fileName: String,
					  file: FileHandle, parameter: TilesetParameter?) : Tileset {
		val json = Json()
		val persistedTileset = json.fromJson(PersistedTileset::class.java, file)

		val texture =
				assetManager.get(file.pathWithoutExtension() + ".png",
						Texture::class.java)

		val tiles = Array<Tile>();
		for (t in persistedTileset.tiles!!) {
      val region = TextureRegion(texture, t.x * 16, t.y * 16, 16, 16)
			tiles.add(Tile(region, t.type!!, t.layer))
		}

		return Tileset(tiles);
	}

	override fun getDependencies(fileName: String, file: FileHandle,
      parameter: TilesetParameter?) : Array<AssetDescriptor<Any>> {
		val dependencies = Array<AssetDescriptor<Any>>()

		val textureDescriptor =
				AssetDescriptor(file.pathWithoutExtension() + ".png",
						Texture::class.java)
		dependencies.add(textureDescriptor as AssetDescriptor<Any>);

		return dependencies
	}

	class TilesetParameter: AssetLoaderParameters<Tileset>()

}