package com.github.fauu.monmonde.ui.sidebar.travel

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.fauu.monmonde.Message

class LocationActionMenu(uiSkin: Skin): Table(), Telegraph {
  private val messenger = MessageManager.getInstance()

  private val selectedRegionLabel =
      Label("", uiSkin, "bold-font", Color.WHITE)
  private val travelToActionLabel =
      Label("Travel to", uiSkin, "small-font", Color.WHITE)

  init {
    messenger.addListeners(this, Message.REGION_LOCATION_SELECTED)

    defaults().top().left().spaceBottom(0f)

    row().padTop(-4f)
    add(selectedRegionLabel)

    row().padBottom(-8f)
    add(travelToActionLabel)

    row().padBottom(-8f)
    val showDescriptionLabel =
        Label("Show description", uiSkin, "small-font", Color(1f, 1f, 1f, .3f))
    add(showDescriptionLabel)
  }

  override fun handleMessage(telegram: Telegram?): Boolean {
    if (telegram == null) return true

    when (telegram.message) {
      Message.REGION_LOCATION_SELECTED -> {
        val name = telegram.extraInfo as String

        selectedRegionLabel.setText(name)

        if (name == "Meadow") {
          travelToActionLabel.setColor(1f, 1f, 0f, 1f)
        } else {
          travelToActionLabel.setColor(1f, 1f, 1f, .3f)
        }
      }
    }

    return true
  }
}
