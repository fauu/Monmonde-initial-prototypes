package com.github.fauu.monmonde.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.github.fauu.monmonde.shared.Assets
import com.github.fauu.monmonde.ui.sidebar.Sidebar

class Ui : Table() {
  init {
    /* Skin */
    val skin = Skin()

    val regularFontGenerator = Assets.FONT_GENERATORS["regular"]!!
    val textFont = regularFontGenerator.generateFont(FONT_PARAMETERS["text"])
//    val mediumFontGenerator = Assets.FONT_GENERATORS["medium"]!!
//    val textFont = mediumFontGenerator.generateFont(FONT_PARAMETERS["text"])
    skin.add("text", textFont, BitmapFont::class.java)

    val boldFontGenerator = Assets.FONT_GENERATORS["bold"]!!
    val optionFont = boldFontGenerator.generateFont(FONT_PARAMETERS["option"])
    skin.add("option", optionFont, BitmapFont::class.java)

    val textLabelStyle = Label.LabelStyle()
    textLabelStyle.font = skin.getFont("text")
    skin.add("default", textLabelStyle, Label.LabelStyle::class.java)

    val optionLabelStyle = Label.LabelStyle()
    optionLabelStyle.font = skin.getFont("option")
    skin.add("option", optionLabelStyle, Label.LabelStyle::class.java)

    val keyLabelStyle = Label.LabelStyle()
    keyLabelStyle.font = skin.getFont("option")
    keyLabelStyle.fontColor = Color.YELLOW
    skin.add("key", keyLabelStyle, Label.LabelStyle::class.java)
    /* end Skin */

    val sidebar = Sidebar(skin)
    sidebar.skin = skin
    val uiPlaceholder = Widget()
    add(uiPlaceholder).width(927f).height(800f).fill()
    add(sidebar.top().left()).width(325f)
        .fill()
        .padTop(8f)
        .padBottom(8f)
        .padLeft(14f)
        .padRight(14f)
    pack()
  }

  companion object {
    val FONT_PARAMETERS =
        mutableMapOf<String, FreeTypeFontGenerator.FreeTypeFontParameter>()

    init {
      var param = FreeTypeFontGenerator.FreeTypeFontParameter()
      with (param) {
        size = 16
        hinting = FreeTypeFontGenerator.Hinting.None
        spaceX = -1
        gamma = 1.5f
      }
      FONT_PARAMETERS["text"] = param

      param = FreeTypeFontGenerator.FreeTypeFontParameter()
      with (param) {
        size = 15
        hinting = FreeTypeFontGenerator.Hinting.None
        spaceX = -1
        gamma = 1.5f
      }
      FONT_PARAMETERS["option"] = param
    }
  }
}