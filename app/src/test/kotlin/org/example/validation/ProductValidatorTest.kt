package org.example.validation

import org.example.model.Accessory
import org.example.model.Oil
import org.example.model.Season
import org.example.model.Tire
import org.example.model.VehicleType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProductValidatorTest {
    private val oil = Oil(1, "Масло", 100.0, 1.0, VehicleType.PASSENGER, volume = 4.0)
    private val tire = Tire(2, "Шина", 1350.0, 0.3, VehicleType.TRUCK, diameter = 16.0, season = Season.SUMMER)
    private val accessory = Accessory(3, "Щетки", 100.0, 0.1, VehicleType.PASSENGER, material = "резина", color = "черный")

    @Test
    fun `accepts correct products`() {
        assertNull(ProductValidator.validate(oil))
        assertNull(ProductValidator.validate(tire))
        assertNull(ProductValidator.validate(accessory))
        assertNull(ProductValidator.validate(accessory.copy(color = null)))
    }

    @Test
    fun `rejects empty name`() {
        assertEquals("Пустое название товара", ProductValidator.validate(oil.copy(name = " ")))
    }

    @Test
    fun `rejects non positive price`() {
        assertEquals("Цена не может быть отрицательной", ProductValidator.validate(tire.copy(priceRub = -450.0)))
        assertEquals("Цена должна быть положительной", ProductValidator.validate(tire.copy(priceRub = 0.0)))
    }

    @Test
    fun `rejects non positive weight`() {
        assertEquals("Вес не может быть отрицательным", ProductValidator.validate(oil.copy(weightKg = -1.0)))
        assertEquals("Вес должен быть положительным", ProductValidator.validate(oil.copy(weightKg = 0.0)))
    }

    @Test
    fun `rejects unknown color`() {
        assertEquals("Недопустимое значение 'color=red'", ProductValidator.validate(accessory.copy(color = "red")))
    }

    @Test
    fun `rejects non positive attributes`() {
        assertEquals("Объем должен быть положительным", ProductValidator.validate(oil.copy(volume = 0.0)))
        assertEquals("Диаметр должен быть положительным", ProductValidator.validate(tire.copy(diameter = -1.0)))
    }
}
