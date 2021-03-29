package com.github.fauu.monmonde

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.Timer
import com.github.fauu.monmonde.screens.battle.BattleScreen

class ScreenManager(private val fader: Fader): Telegraph {
  private val messenger = MessageManager.getInstance()

  val screens = mutableListOf<Screen>()

  var activeScreen: Screen? = null
    set(screen) {
      field = screen

      if (screen != null) {
        screen.prepare()
        messenger.dispatchMessage(Message.ACTIVE_SCREEN_CHANGED, screen)
      }
    }

  init {
    messenger.addListener(this, Message.ACTIVE_SCREEN_CHANGE_REQUESTED)
  }

  override fun handleMessage(telegram: Telegram?): Boolean {
    if (telegram == null) return true

    when (telegram.message) {
      Message.ACTIVE_SCREEN_CHANGE_REQUESTED -> {
        val newScreen = telegram.extraInfo as Class<out Screen>

        val options =
          when (newScreen) {
            BattleScreen::class.java ->
              TransitionOptions(Color.WHITE, 1f, Interpolation.exp5In, Interpolation.exp5Out)
            else ->
              TransitionOptions(Color.BLACK, .5f, Interpolation.fade)
          }

        fader.run(Fader.FadeAction(Fader.FadeType.FADE_OUT, options.color,
            options.duration, options.outInterpolation))

        Timer.schedule(object: Timer.Task() {
          override fun run() = set(newScreen)
        }, options.duration)

        Timer.schedule(object: Timer.Task() {
          override fun run() =
              fader.run(Fader.FadeAction(Fader.FadeType.FADE_IN, options.color,
                  1f, options.inInterpolation))
        }, options.duration)
      }
    }

    return true
  }

  fun register(screen: Screen) {
    screens.add(screen)
  }

  fun set(screenType: Class<out Screen>) {
    activeScreen = screens.first { screenType.isAssignableFrom(it.javaClass) }
    activeScreen?.activate()
  }

  data class TransitionOptions(val color: Color,
                               val duration: Float,
                               val outInterpolation: Interpolation = Interpolation.linear,
                               val inInterpolation: Interpolation = Interpolation.linear)
}