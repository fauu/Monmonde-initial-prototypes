package com.github.fauu.monmonde.sim

import com.badlogic.gdx.Gdx
import com.github.fauu.monmonde.sim.ui.Ui
import com.github.fauu.monmonde.sim.util.getFormattedCurrentDateTime
import com.github.fauu.monmonde.sim.util.getFormattedDateTime
import com.github.fauu.monmonde.sim.util.parseDate
import com.github.michaelbull.result.*

private typealias CommandFunction = (List<String>) -> Result<String?, String>

private val COMMAND_NOT_FOUND_ERROR = Err("Command not found")

class Controller(private val simulationManager: SimulationManager) {
  private val commands = hashMapOf<String, CommandFunction>(
    "advance" to top@{ args ->
      when (args.size) {
        1 -> {
          val date = parseDate(args[0]).getOrElse {
            return@top Err("Expected date in ISO format")
          }
          simulationManager.simulation.advanceTo(date)
        }
        else -> COMMAND_NOT_FOUND_ERROR
      }
    },
    "clear" to { _ ->
      Ui.clearOutput()
      Ok(null)
    },
    "exit" to { _ ->
      Gdx.app.exit()
      Ok(null)
    },
    "init" to { _ ->
      simulationManager.initSimulation()
    },
    "sys" to { args ->
      if (args.isEmpty()) {
        COMMAND_NOT_FOUND_ERROR
      } else {
        when (args[0]) {
          "time" -> Ok(getFormattedCurrentDateTime())
          else -> COMMAND_NOT_FOUND_ERROR
        }
      }
    },
    "time" to { _ ->
      Ok(getFormattedDateTime(simulationManager.simulation.dateTime))
    }
  )

  fun executeCommand(commandString: String) {
    Ui.out("> $commandString")
    val (name, args) =
        commandString
          .trim()
          .split(' ')
          .let { Pair(it.first(), it.drop(1)) }
    commands[name]
      .toResultOr { "Command not found" }
      .flatMap { it(args) }
      .mapBoth(
        { okMsg -> okMsg?.run { if (isNotBlank()) Ui.out(this) } },
        { errorMsg -> errorMsg.also { Ui.err(it) }.also { println(it) } }
      )
  }
}