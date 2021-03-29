package com.github.fauu.monmonde.ui.sidebar.exploration

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.github.fauu.monmonde.Message
import com.github.fauu.monmonde.screens.exploration.AreaMon
import java.util.*

class VisibleMonList(val uiSkin: Skin): VerticalGroup(), Telegraph {
  private val messenger = MessageManager.getInstance()

  private var selectedEntryNo: Int = -1
  private var visibleMons = ArrayList<AreaMon>()

  private val isMonSelected: Boolean
    get() = selectedMon != null

  private val selectedMon: AreaMon?
    get() = visibleMons.getOrNull(selectedEntryNo)

  init {
    messenger.addListener(this, Message.VISIBLE_MONS_CHANGED)
    space(-4f)
  }

  private fun entryLabelNo(no: Int): Label {
    return (children[no] as Table).children[1] as Label
  }

  override fun handleMessage(msg: Telegram?): Boolean {
    if (msg == null) return true

    when (msg.message) {
      Message.VISIBLE_MONS_CHANGED -> {
        val previousSelectedMon = visibleMons.getOrNull(selectedEntryNo)

        clearChildren()
        visibleMons = msg.extraInfo as ArrayList<AreaMon>

        for (mon in visibleMons) {
          val table = Table()

          val image = Image(mon.mon.kind.uiTR)
          table.add(image).size(32f, 32f).spaceRight(10f)

          val label = Label(mon.displayName, uiSkin, "small-font", Color.WHITE)

          table.add(label).padBottom(7f)
          table.pack()

          addActor(table)
        }

        selectedEntryNo = visibleMons.indexOf(previousSelectedMon)
        if (selectedEntryNo == -1 && visibleMons.size > 0)
          selectedEntryNo = 0

        if (isMonSelected) {
          entryLabelNo(selectedEntryNo).color = Color.YELLOW
        }
        messenger.dispatchMessage(Message.VISIBLE_MON_SELECTED,
            visibleMons.getOrNull(selectedEntryNo))
      }
    }

    return true
  }

  private fun processInput() {
    val numEntries = children.size

    if (numEntries == 0) return

    var newSelectedEntryNo: Int? = null

    when {
      Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> {
        newSelectedEntryNo = (selectedEntryNo + 1) % numEntries
      }
      Gdx.input.isKeyJustPressed(Input.Keys.UP) -> {
        newSelectedEntryNo = (numEntries + (selectedEntryNo - 1)) % numEntries
      }
      isMonSelected && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) -> {
        messenger.dispatchMessage(Message.ENCOUNTER_INITIATED, selectedMon!!)
      }
    }

    if (newSelectedEntryNo != null) {
      if (isMonSelected)
        entryLabelNo(selectedEntryNo).color = Color.WHITE

      entryLabelNo(newSelectedEntryNo).color = Color.YELLOW

      selectedEntryNo = newSelectedEntryNo
      messenger.dispatchMessage(Message.VISIBLE_MON_SELECTED,
          visibleMons[newSelectedEntryNo])
    }
  }

  override fun draw(batch: Batch?, parentAlpha: Float) {
    processInput()
    super.draw(batch, parentAlpha)
  }
}

