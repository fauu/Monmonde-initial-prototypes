package com.github.fauu.monmonde.sim

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.github.fauu.monmonde.sim.ui.Ui
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MonmondeSim : ApplicationAdapter() {
  private val simulationManager = SimulationManager()
  private val controller = Controller(simulationManager)
  private var commandInputHistory = mutableListOf<String>()
  private var commandInputHistoryCurrent = 0

  override fun create() {
    Gdx.app.logLevel = Application.LOG_DEBUG

    Ui.run {
      create()
      onSubmit {
        commandInputHistory.add(it)
        commandInputHistoryCurrent = commandInputHistory.size
        controller.executeCommand(it)
      }
      onInputHistoryNextRequested {
        // TODO: Maybe use something like Guava's EvictingQueue to limit the number of entries
        if (++commandInputHistoryCurrent >= commandInputHistory.size) {
          commandInputHistoryCurrent = commandInputHistory.size - 1
        }
        setUiInputFromCommandInputHistory()
      }
      onInputHistoryPrevRequested {
        if (--commandInputHistoryCurrent < 0) commandInputHistoryCurrent = 0
        setUiInputFromCommandInputHistory()
      }
    }

    thread(start = true) {
      Ui.out("Welcome to Monmonde Simulation")
      controller.executeCommand("init")
      measureTimeMillis {
        controller.executeCommand("advance 2000-11-21")
      }.also { Ui.out("Advancing the simulation took $it milliseconds.") }
    }
  }

  private fun setUiInputFromCommandInputHistory() {
    commandInputHistory
      .getOrNull(commandInputHistoryCurrent)
      ?.let { Ui.setInput(it) }
  }

  override fun render() {
    Gdx.gl.glClearColor(1f, 0f, 1f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Ui.render()
  }

  override fun resize(width: Int, height: Int) = Ui.resize(width, height)

  override fun dispose() = Ui.dispose()
}
