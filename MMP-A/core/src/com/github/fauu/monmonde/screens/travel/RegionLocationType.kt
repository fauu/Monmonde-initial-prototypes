package com.github.fauu.monmonde.screens.travel

enum class RegionLocationType(val displayName: String) {
	HOME("Home"),
	BUS_STOP("Bus stop"),
	MEADOW("Meadow"),
	FOREST("Forest");
	
	override fun toString() = displayName
}