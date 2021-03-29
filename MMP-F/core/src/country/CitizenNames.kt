package com.github.fauu.monmonde.sim.country

import com.github.fauu.monmonde.sim.actor.PersonName
import com.github.fauu.monmonde.sim.actor.Sex
import java.util.*

data class CitizenNames(
  private val sexToRawNames: Map<Sex, LinkedList<String>> =
    mapOf(Sex.MALE to LinkedList(), Sex.FEMALE to LinkedList())
) {
  val size get() = 2 * sexToRawNames[Sex.MALE]!!.size

  operator fun get(sex: Sex) = sexToRawNames[sex]!!

  operator fun set(sex: Sex, names: List<String>) {
    sexToRawNames[sex]!!.run {
      clear()
      addAll(names)
    }
  }

  fun pop(sex: Sex): PersonName {
    return sexToRawNames
      .also { check(it[sex] != null) }
      .also { check(it[sex]!!.isNotEmpty()) }
      .let { this[sex].remove().split('|') }
      .let { nameParts -> PersonName(nameParts[0], nameParts[1]) }
  }
}
