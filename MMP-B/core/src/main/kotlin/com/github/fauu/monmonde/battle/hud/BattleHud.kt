package com.github.fauu.monmonde.battle.hud

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Group
import com.github.fauu.monmonde.shared.Assets
import com.github.fauu.monmonde.battle.types.BattleSpot
import com.github.fauu.monmonde.shared.Message
import com.github.fauu.monmonde.shared.types.MonMove

class BattleHud(): Group(), Telegraph {
  private val messenger = MessageManager.getInstance()

  private lateinit var monNameFont: BitmapFont
  private lateinit var monMoveFont: BitmapFont
  private val monPlates = mutableMapOf<BattleSpot, MonPlate>()

  init {
    messenger.addListeners(this,
        Message.MON_ENTERED_BATTLE,
        Message.MON_MOVE_BEGAN,
        Message.MON_MOVE_FINISHED,
        Message.MON_FATIGUE_CHANGED)

    val boldFontGenerator = Assets.FONT_GENERATORS["bold"]!!
    monNameFont = boldFontGenerator.generateFont(FONT_PARAMETERS["monName"])
    monMoveFont = boldFontGenerator.generateFont(FONT_PARAMETERS["monMove"])
  }

  override fun handleMessage(msg: Telegram?): Boolean {
    if (msg == null) return true

    when (msg.message) {
      Message.MON_ENTERED_BATTLE -> {
        @Suppress("UNCHECKED_CAST")
        val extraInfo = msg.extraInfo as Pair<BattleSpot, String>
        val (battlePosition, name) = extraInfo

        val monPlate = MonPlate(monNameFont, monMoveFont, name)

        with (monPlate) {
          when (battlePosition) {
            BattleSpot.FOREGROUND -> {
              x = 86f
              y = 348f
            }
            BattleSpot.BACKGROUND -> {
              x = 618f
              y = 491f
            }
          }
        }

        monPlates.put(battlePosition, monPlate)
        addActor(monPlate)
      }
      Message.MON_MOVE_BEGAN -> {
        val (battlePosition, move) = msg.extraInfo as Pair<BattleSpot, MonMove>

        monPlates[battlePosition]!!.displayMove(move)
      }
      Message.MON_MOVE_FINISHED -> {
//        val (battlePosition, restTime) = msg.extraInfo as Pair<BattlePositionType, Float>
        val battlePosition = msg.extraInfo as BattleSpot

        monPlates[battlePosition]!!.displayHold()
      }
      Message.MON_FATIGUE_CHANGED -> {
        @Suppress("UNCHECKED_CAST")
        val info = msg.extraInfo as Pair<BattleSpot, Float>
        val (battlePosition, fatigueReservePercent) = info

        monPlates[battlePosition]!!.updateFatigueBar(fatigueReservePercent)
      }
    }

    return true
  }

  companion object {
    val FONT_PARAMETERS =
        mutableMapOf<String, FreeTypeFontGenerator.FreeTypeFontParameter>()

    init {
      var param = FreeTypeFontGenerator.FreeTypeFontParameter()
      with (param) {
        size = 16
        shadowOffsetX = 2
        shadowOffsetY = 2
        borderWidth = 2f
        hinting = FreeTypeFontGenerator.Hinting.None
        gamma = 1f
        spaceX = -2
      }
      FONT_PARAMETERS["monName"] = param

      param = FreeTypeFontGenerator.FreeTypeFontParameter()
      with (param) {
        size = 14
        shadowOffsetX = 1
        shadowOffsetY = 1
        borderWidth = 0f
        hinting = FreeTypeFontGenerator.Hinting.None
        gamma = 1f
        spaceX = -1
      }
      FONT_PARAMETERS["monMove"] = param
    }
  }
}

