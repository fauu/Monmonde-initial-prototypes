package com.github.fauu.monmonde.shared.operations

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.graphics.Color
import com.github.fauu.monmonde.battle.components.HasColorization
import se.feomedia.orion.Executor
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

class ColorizeOperation: TemporalOperation() {
  val initial = Color()
  val final = Color()

  override fun executorType(): Class<out Executor<*>> {
    return ColorizeExecutor::class.java
  }

  override fun reset() {
    super.reset()
    initial.set(Color.WHITE)
    final.set(Color.WHITE)
  }

  @Wire
  class ColorizeExecutor:
      TemporalOperation.TemporalExecutor<ColorizeOperation>() {
    private lateinit var mHasColorization: ComponentMapper<HasColorization>

    override fun begin(op: ColorizeOperation?, node: OperationTree?) {
      super.begin(op, node)
      op!!.initial.set(mHasColorization[op.entityId].colorization)
    }

    override fun act(delta: Float,
                     percent: Float,
                     op: ColorizeOperation?,
                     node: OperationTree?) {
      val color = mHasColorization[op!!.entityId].colorization
      color.set(op.initial.lerp(op.final, percent))
    }
  }
}