package com.github.fauu.monmonde.exploration.components

import com.artemis.Component
import com.github.fauu.monmonde.exploration.types.AreaMap

class HasAreaMap(): Component() {
  lateinit var areaMap: AreaMap
}