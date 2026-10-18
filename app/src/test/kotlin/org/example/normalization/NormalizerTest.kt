package org.example.normalization

import org.example.model.Currency
import org.example.model.Price
import org.example.model.Weight
import org.example.model.WeightUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class NormalizerTest {
    @Test
    fun `converts dollars to rubles`() {
        assertEquals(1125.0, Normalizer.toRubles(Price(12.5, Currency.USD)))
    }

    @Test
    fun `keeps rubles as is`() {
        assertEquals(300.5, Normalizer.toRubles(Price(300.5, Currency.RUB)))
    }

    @Test
    fun `converts pounds to kilograms`() {
        assertEquals(4.54, Normalizer.toKilograms(Weight(10.0, WeightUnit.LB)))
        assertEquals(0.91, Normalizer.toKilograms(Weight(2.0, WeightUnit.LB)))
    }

    @Test
    fun `keeps kilograms as is`() {
        assertEquals(0.05, Normalizer.toKilograms(Weight(0.05, WeightUnit.KG)))
    }
}
