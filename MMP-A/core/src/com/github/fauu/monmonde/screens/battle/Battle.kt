package com.github.fauu.monmonde.screens.battle

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Timer
import com.github.fauu.monmonde.Mon
import com.github.fauu.monmonde.camera.MoveAction
import com.github.fauu.monmonde.camera.ZoomAction

class Battle(_playerMon: Mon, _foeMon: Mon, camera: OrthographicCamera): Group() {
  val playerMon = BattleMon(_playerMon, Position.PLAYER)
  val foeMon = BattleMon(_foeMon, Position.FOE)

  init {
    foeMon.setScale(2f)
    foeMon.setPosition(690f, 320f)
    addActor(foeMon)

    playerMon.setScale(.01f)
    playerMon.setOrigin(playerMon.width / 2, 20f)
    playerMon.setPosition(110f, -45f)
    addActor(playerMon)

    addAction(MoveAction(camera, Vector2(752f, 416f)))
    addAction(ZoomAction(camera, 0.01f))

    addAction(ZoomAction(camera, 1f, 1f, Interpolation.exp5Out))
    addAction(MoveAction(camera, Vector2(928f / 2, 800f / 2), 1f, Interpolation.exp5Out))
    Timer.schedule(object: Timer.Task() {
      override fun run() = playerMon.addAction(Actions.scaleTo(5.5f, 5.5f, .15f, Interpolation.exp5Out))
    }, .8f)
  }

  enum class Position {
    PLAYER, FOE
  }
}
