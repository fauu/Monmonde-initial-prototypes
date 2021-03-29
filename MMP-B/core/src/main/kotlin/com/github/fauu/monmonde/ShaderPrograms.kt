package com.github.fauu.monmonde

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object ShaderPrograms {
  fun create(name: String): ShaderProgram {
    val vertex = Gdx.files.internal("shaders/$name.vertex.glsl").readString()
    val fragment = Gdx.files.internal("shaders/$name.fragment.glsl")
        .readString()

    val program = ShaderProgram(vertex, fragment)
    if (!program.isCompiled) Gdx.app.log("SHADER", program.log)

    return program
  }
}