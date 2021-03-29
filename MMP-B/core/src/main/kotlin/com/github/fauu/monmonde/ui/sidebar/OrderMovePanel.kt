package com.github.fauu.monmonde.ui.sidebar

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.fauu.monmonde.shared.Message
import com.github.fauu.monmonde.shared.types.MonMove

class OrderMovePanel(skin: Skin): Table(skin), Telegraph {
  private val messenger = MessageManager.getInstance()

  private val moveList = Table()

  init {
    messenger.addListeners(this,
        Message.PLAYER_AVAILABLE_MOVES_CHANGED,
        Message.PLAYER_MOVE_ORDERING_STATE_CHANGED)
    left()

    add(moveList)

    pack()
  }

  private fun rebuildMoveList(moves: List<MonMove>) {
    val optionLabelStyle: Label.LabelStyle =
        skin["option", Label.LabelStyle::class.java]
    val keyLabelStyle: Label.LabelStyle =
        skin["key", Label.LabelStyle::class.java]

    moveList.clear()

    moves.forEachIndexed { i, move ->
      moveList.row().left()

      val moveEntry = Table()
      // TODO: Sync with actual keys from MoveOrderingSystem
      moveEntry.add(Label(KEYS[i], keyLabelStyle)).width(25f)
      moveEntry.add(Label(move.toString().toUpperCase(), optionLabelStyle))
      moveList.add(moveEntry)
    }
  }

  override fun handleMessage(telegram: Telegram?): Boolean {
    if (telegram == null) return true

    when (telegram.message) {
      Message.PLAYER_AVAILABLE_MOVES_CHANGED -> {
        @Suppress("UNCHECKED_CAST")
        val moves = telegram.extraInfo as List<MonMove>

        rebuildMoveList(moves)
      }
      Message.PLAYER_MOVE_ORDERING_STATE_CHANGED -> {
        val canOrder = telegram.extraInfo as Boolean

        val newAlpha = if (canOrder) 1f else .4f
        addAction(Actions.alpha(newAlpha, .05f))
      }
    }

    return true
  }

  companion object {
    // TODO: Sync with actual keys from MoveOrderingSystem
    val KEYS = listOf("Q", "W", "E", "R")
  }
}