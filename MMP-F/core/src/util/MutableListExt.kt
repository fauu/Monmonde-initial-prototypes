package com.github.fauu.monmonde.sim.util

fun <T> MutableList<T>.swap(idx1: Int, idx2: Int) {
  if (idx1 == idx2) return
  val tmp = this[idx1]
  this[idx1] = this[idx2]
  this[idx2] = tmp
}

