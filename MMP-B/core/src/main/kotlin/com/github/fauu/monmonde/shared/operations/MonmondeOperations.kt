package com.github.fauu.monmonde.shared.operations

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import se.feomedia.orion.OperationFactory.*

object MonmondeOperations {
  @JvmOverloads fun colorize(color: Color, duration: Float = 0f): ColorizeOperation {
    val op = operation(ColorizeOperation::class.java)

    configure(op, duration, Interpolation.linear)
    op.final.set(color)

    return op
  }
}

