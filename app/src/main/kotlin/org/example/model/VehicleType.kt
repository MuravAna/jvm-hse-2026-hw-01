package org.example.model

enum class VehicleType {
    PASSENGER,
    TRUCK,
    MOTORCYCLE;

    companion object {
        fun fromName(name: String): VehicleType? = entries.firstOrNull { it.name == name }
    }
}
