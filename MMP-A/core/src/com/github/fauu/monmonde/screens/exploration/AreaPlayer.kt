package com.github.fauu.monmonde.screens.exploration

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.fauu.monmonde.Message

class AreaPlayer(private val area: Area, textureRegion: TextureRegion)
    : Actor(), AreaEntity, Telegraph {
  private val MOVE_BASE_DURATION = .3f

  private val messenger = MessageManager.getInstance()

  override val worldPosition = GridPoint2(0, 0)

	private val sprite = Sprite(textureRegion)
  private var activeMoveAction: MoveAction? = null

	init {
		setBounds(sprite.x, sprite.y, sprite.width, sprite.height)
	}

  fun moveTo(x: Int, y: Int, duration: Float = 0f) {
    addAction(Actions.moveTo(x * 16f, y * 16f, duration))
    worldPosition.set(x, y)
  }

  fun moveBy(x: Int, y: Int, duration: Float = 0f) {
    addAction(Actions.moveBy(x * 16f, y * 16f, duration))
    worldPosition.add(x, y)
  }

	override fun positionChanged() {
		sprite.setPosition(x, y)
		super.positionChanged()
	}

	override fun draw(batch: Batch, parentAlpha: Float) {
		sprite.draw(batch)
	}

  private fun processInput(delta: Float) {
    val moveVector = GridPoint2()

    // TODO: Move to a seperate Move class (intermediary) so as to not depend on
    // the area object?
    fun move() {
      activeMoveAction = MoveAction()
      var targetTileCoords = GridPoint2(worldPosition.x + moveVector.x,
          worldPosition.y + moveVector.y)
      val targetTileSpeed = area.speedAt(targetTileCoords)

      if (targetTileSpeed == 0f) return

      val directionCoeff =
          if (moveVector.x != 0 && moveVector.y != 0) 0.707f else 1f

      val duration = MOVE_BASE_DURATION / (targetTileSpeed * directionCoeff)
      moveBy(moveVector.x, moveVector.y, duration)
      activeMoveAction!!.duration = duration

      messenger.dispatchMessage(Message.AREA_PLAYER_MOVED)
    }

    if (activeMoveAction != null) {
      with (activeMoveAction!!) {
        elapsed += delta
        if (elapsed < duration) return
        else activeMoveAction = null
      }
    } else {
      if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        moveVector.y += 1
      }
      if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        moveVector.y += -1
      }
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        moveVector.x += -1
      }
      if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        moveVector.x += 1
      }

      if (!moveVector.isZero()) move()
    }
  }

  override fun handleMessage(telegram: Telegram?): Boolean = true

  // TODO: Move to separate a Extensions unit
  fun GridPoint2.isZero(): Boolean = (this.x == 0 && this.y == 0)

	override fun act(delta: Float) {
    processInput(delta)
		super.act(delta)
	}

  class MoveAction() {
    var elapsed = 0f
    var duration = 0f
  }
}