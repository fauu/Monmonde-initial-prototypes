package com.github.fauu.monmonde.sim.actor

data class PersonName(val first: String, val last: String) {
  override fun toString() = "$first $last"
}