package org.example.model

enum class Currency {
    USD,
    RUB
}

enum class WeightUnit {
    KG,
    LB
}

data class Price(val amount: Double, val currency: Currency)

data class Weight(val amount: Double, val unit: WeightUnit)
