package org.example.export

import org.example.model.Accessory
import org.example.model.Oil
import org.example.model.Season
import org.example.model.Tire
import org.example.model.VehicleType

object TestProducts {
    val oil = Oil(1, "Mobil 1, Synthetic", 1125.0, 4.54, VehicleType.PASSENGER, volume = 4.0)
    val tire = Tire(3, "Michelin Primacy", 1350.0, 0.3, VehicleType.PASSENGER, diameter = 16.0, season = Season.SUMMER)
    val accessory = Accessory(5, "Щетки", 100.0, 0.1, VehicleType.PASSENGER, material = "резина", color = null)
    val all = listOf(oil, tire, accessory)
}
