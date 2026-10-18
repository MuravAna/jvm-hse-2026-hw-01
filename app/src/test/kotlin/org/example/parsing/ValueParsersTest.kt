package org.example.parsing

import org.example.error.ParseException
import org.example.model.Currency
import org.example.model.Price
import org.example.model.ProductType
import org.example.model.Weight
import org.example.model.WeightUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValueParsersTest {
    @Test
    fun `parses prices in different formats`() {
        assertEquals(Price(12.5, Currency.USD), ValueParsers.parsePrice("$12.5"))
        assertEquals(Price(15.0, Currency.USD), ValueParsers.parsePrice("15$"))
        assertEquals(Price(300.5, Currency.RUB), ValueParsers.parsePrice("300.5руб"))
        assertEquals(Price(-5.0, Currency.USD), ValueParsers.parsePrice("-5$"))
    }

    @Test
    fun `fails on invalid price`() {
        val error = assertFailsWith<ParseException> { ValueParsers.parsePrice("abc") }
        assertEquals("Не удалось распарсить цену 'abc'", error.message)
        assertFailsWith<ParseException> { ValueParsers.parsePrice("abc$") }
        assertFailsWith<ParseException> { ValueParsers.parsePrice("") }
    }

    @Test
    fun `parses weights in different units`() {
        assertEquals(Weight(1.2, WeightUnit.KG), ValueParsers.parseWeight("1.2kg"))
        assertEquals(Weight(10.0, WeightUnit.LB), ValueParsers.parseWeight("10lb"))
    }

    @Test
    fun `fails on invalid weight`() {
        assertFailsWith<ParseException> { ValueParsers.parseWeight("10") }
        assertFailsWith<ParseException> { ValueParsers.parseWeight("xkg") }
    }

    @Test
    fun `keeps only allowed extra keys`() {
        assertEquals(
            mapOf("volume" to "4"),
            ValueParsers.parseExtra("viscosity=5W-30;volume=4", ProductType.OIL),
        )
        assertEquals(
            mapOf("material" to "резина", "color" to "черный"),
            ValueParsers.parseExtra("material=резина;color=черный;diameter=5", ProductType.ACCESSORY),
        )
        assertEquals(emptyMap(), ValueParsers.parseExtra("", ProductType.TIRE))
    }

    @Test
    fun `fails on malformed extra`() {
        assertFailsWith<ParseException> { ValueParsers.parseExtra("volume", ProductType.OIL) }
    }
}
