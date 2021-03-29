package com.github.fauu.monmonde.screens.travel

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json

import com.github.fauu.monmonde.persistence.PersistedRegion

class RegionMapLoader(resolver: FileHandleResolver)
    : SynchronousAssetLoader<RegionMap, RegionMapLoader.MapParameter>(resolver) {
	override fun load(assetManager: AssetManager, fileName: String,
					  file: FileHandle, parameter: MapParameter?) : RegionMap {
		val json = Json()
		json.setIgnoreUnknownFields(true)
		val persistedRegion = json.fromJson(PersistedRegion::class.java, file)

		val texture =
				assetManager.get(file.pathWithoutExtension() + ".png",
						Texture::class.java)

		val locations = Array<RegionLocation>();
		for (l in persistedRegion.locations!!) {
			locations.add(RegionLocation(l.type!!, l.x, l.y))
		}

		return RegionMap(texture, locations);
	}

	override fun getDependencies(fileName: String, file: FileHandle,
      parameter: MapParameter?) : Array<AssetDescriptor<Any>> {
		val dependencies = Array<AssetDescriptor<Any>>()

		val textureDescriptor =
				AssetDescriptor(file.pathWithoutExtension() + ".png",
						Texture::class.java)
		dependencies.add(textureDescriptor as AssetDescriptor<Any>);

		return dependencies
	}

	class MapParameter : AssetLoaderParameters<RegionMap>()

}