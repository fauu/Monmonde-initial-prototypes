package com.github.fauu.monmonde.sim.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.actors.onKey
import ktx.actors.onKeyDown
import ktx.assets.load
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.scene2d.textField
import ktx.style.label
import ktx.style.skin
import ktx.style.textField

object Ui : Stage() {
  private val BG_COLOR = Color(7 / 255f, 66 / 255f, 27 / 255f, 1f)
  private val BG_COLOR_2 = Color(6 / 255f, 38 / 255f, 14 / 255f, 1f)
  private val TEXT_COLOR = Color(0.9f, 0.9f, 0.9f, 1f)
  private const val FONT_PATH = "RobotoMono-Medium.ttf"
  private const val CURSOR_PATH = "cursor.png"
  private const val BLANK_PATH = "blank.png"
  private const val OUTPUT_TEXT_MAX_LINE_COUNT = 150

  private lateinit var assetManager: AssetManager
  private lateinit var root: Table
  private lateinit var outputLabel: Label
  private lateinit var inputField: TextField
  private lateinit var submitCallback: (String) -> Unit
  private lateinit var inputHistoryPrevRequestedCallback: () -> Unit
  private lateinit var inputHistoryNextRequestedCallback: () -> Unit

  fun create() {
    assetManager = AssetManager().apply {
      registerFreeTypeFontLoaders()
      loadFreeTypeFont(FONT_PATH) {
        size = 20
        hinting = FreeTypeFontGenerator.Hinting.None
        gamma = 1f
        shadowOffsetX = 1
        shadowOffsetY = 1
        shadowColor = Color(0f, 0f, 0f, 0.3f)
      }
      load<Texture>(CURSOR_PATH)
      load<Texture>(BLANK_PATH)
      finishLoading()
    }
    val defaultFont = assetManager.get<BitmapFont>(FONT_PATH)

    viewport = ScreenViewport()
    Gdx.input.inputProcessor = this

    Scene2DSkin.defaultSkin = skin {
      label {
        font = defaultFont
        fontColor = TEXT_COLOR
      }
      textField {
        font = defaultFont
        fontColor = TEXT_COLOR
        cursor = TextureRegionDrawable(TextureRegion(assetManager.get<Texture>(CURSOR_PATH))).tint(
          TEXT_COLOR
        )
      }
    }

    root = table {
      background =
          TextureRegionDrawable(TextureRegion(assetManager.get<Texture>(BLANK_PATH))).tint(BG_COLOR)

      setFillParent(true)
      bottom()

      outputLabel = label("").cell(colspan = 2, growX = true, pad = 8f)

      row()

      table {
        background =
            TextureRegionDrawable(TextureRegion(assetManager.get<Texture>(BLANK_PATH))).tint(
              BG_COLOR_2
            )
        pad(8f)

        label(">").cell(padRight = 8f)

        inputField = textField()
        inputField.cell(growX = true)
      }.cell(growX = true)
    }

    inputField.onKey {
      if (it == '\r' && inputField.text.isNotBlank()) {
        submitCallback(inputField.text)
        inputField.text = ""
      }
    }
    inputField.onKeyDown {
      when (it) {
        Input.Keys.UP -> inputHistoryPrevRequestedCallback()
        Input.Keys.DOWN -> inputHistoryNextRequestedCallback()
      }
    }
    keyboardFocus = inputField

    addActor(root)
  }

  fun render() {
    viewport.apply()
    act(Gdx.graphics.deltaTime)
    draw()
  }

  fun resize(width: Int, height: Int) {
    viewport.update(width, height, true)
  }

  override fun dispose() {
    assetManager.dispose()
    super.dispose()
  }

  private fun print(msg: String) {
    val currentTextLines = outputLabel.text.lines()
    val linesToDropCount =
      if (currentTextLines.size + 1 > OUTPUT_TEXT_MAX_LINE_COUNT) {
        currentTextLines.size - OUTPUT_TEXT_MAX_LINE_COUNT + 1
      } else {
        0
      }
    val newText = "${currentTextLines.drop(linesToDropCount).joinToString("\n")}\n$msg"
    outputLabel.setText(newText)
  }

  fun out(msg: String) = print(msg)

  fun err(msg: String) = print("ERROR: $msg")

  fun clearOutput() = outputLabel.setText("")

  fun setInput(content: String) {
    inputField.text = content
    inputField.cursorPosition = content.length
  }

  fun onSubmit(callback: (text: String) -> Unit) {
    submitCallback = callback
  }

  fun onInputHistoryPrevRequested(callback: () -> Unit) {
    inputHistoryPrevRequestedCallback = callback
  }

  fun onInputHistoryNextRequested(callback: () -> Unit) {
    inputHistoryNextRequestedCallback = callback
  }

}