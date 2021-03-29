package com.github.fauu.monmonde.screens.travel

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.github.fauu.monmonde.Assets
import com.github.fauu.monmonde.Message
import com.github.fauu.monmonde.Screen

class TravelScreen(batch: SpriteBatch, private val skin: Skin) : Screen(batch) {
  override fun activate() {
  }

  companion object {
    val ASSET_DEPENDENCIES = listOf(Assets.REGION_MAP)
  }

  private var locationLabels = Array<RegionLocationLabel>()
	private var numLocations = 0
	private var selectedLocationNo = 0
	private var locationSelectionTimePassed = 0f
	
	override fun prepare() {
    if (ready) return

    Assets.load(ASSET_DEPENDENCIES)

    viewport = ScalingViewport(Scaling.none, 928f, 800f, OrthographicCamera())
		stage = Stage(viewport, batch)

		val regionMap = Assets.get(Assets.REGION_MAP)
		stage.addActor(regionMap)
		
		val locationLabelGroup = Group();
		for (location in regionMap.locations) {
			val type = location.type

      val locationLabel = RegionLocationLabel(type.toString(), skin)

      locationLabel.x = location.x.toFloat()
      locationLabel.y = location.y.toFloat()
			
			locationLabelGroup.addActor(locationLabel);
			locationLabels.add(locationLabel)
		}
		
		locationLabels.sort({ a, b -> a.y.toInt() - b.y.toInt() })
		
		selectedLocationNo = locationLabels.indexOfFirst { it.text == "Home" }
		locationLabels.get(selectedLocationNo).selected = true
		
		messenger.dispatchMessage(Message.REGION_LOCATION_SELECTED, "Home")
		
		numLocations = locationLabels.size
    stage.addActor(locationLabelGroup)

    ready = true
	}

  override fun handleMessage(telegram: Telegram?): Boolean = true

	override fun processInput(delta: Float) {
		fun setSelectedLocationNo(no: Int) {
      locationLabels.get(selectedLocationNo).selected = false

      selectedLocationNo = no

			val selectedLocationLabel = locationLabels.get(selectedLocationNo)
			selectedLocationLabel.selected = true

      messenger.dispatchMessage(0f, Message.REGION_LOCATION_SELECTED,
			    selectedLocationLabel.text)
		}

		var newSelectedLocationNo: Int? = null
		
    when {
      Gdx.input.isKeyJustPressed(Input.Keys.W) -> {
        newSelectedLocationNo = (selectedLocationNo + 1) % numLocations
      }
      Gdx.input.isKeyJustPressed(Input.Keys.S) -> {
        newSelectedLocationNo =
            (numLocations + (selectedLocationNo - 1)) % numLocations
      }
    }
		
		if (newSelectedLocationNo != null) {
		  setSelectedLocationNo(newSelectedLocationNo)
      locationSelectionTimePassed = 0f

      return
		}
		
		locationSelectionTimePassed += delta
		
		if (locationSelectionTimePassed < .15f) return

    when {
      Gdx.input.isKeyPressed(Input.Keys.W) -> {
        newSelectedLocationNo = (selectedLocationNo + 1) % numLocations
      }
      Gdx.input.isKeyPressed(Input.Keys.S) -> {
        newSelectedLocationNo =
            (numLocations + (selectedLocationNo - 1)) % numLocations
      }
    }

		if (newSelectedLocationNo != null) {
		  setSelectedLocationNo(newSelectedLocationNo)
		}
    
    locationSelectionTimePassed = 0f
	}

  override fun render(delta: Float) {
    if (!ready) return

    processInput(delta)

    viewport.setScreenPosition(0, 0)
    viewport.apply()

    stage.act(delta)
    stage.draw()
  }

	override fun dispose() = super.dispose()
}