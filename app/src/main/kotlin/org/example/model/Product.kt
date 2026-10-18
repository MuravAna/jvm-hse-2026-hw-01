package org.example.model

sealed class Product {
    abstract val id: Int
    abstract val name: String
    abstract val priceRub: Double
    abstract val weightKg: Double
    abstract val vehicle: VehicleType
    abstract val type: ProductType
}

data class Oil(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: VehicleType,
    val volume: Double?,
) : Product() {
    override val type: ProductType get() = ProductType.OIL
}

data class Tire(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: VehicleType,
    val diameter: Double?,
    val season: Season?,
) : Product() {
    override val type: ProductType get() = ProductType.TIRE
}

data class Accessory(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: VehicleType,
    val material: String?,
    val color: String?,
) : Product() {
    override val type: ProductType get() = ProductType.ACCESSORY
}
