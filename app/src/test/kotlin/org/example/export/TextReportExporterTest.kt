package org.example.export

import kotlin.test.Test
import kotlin.test.assertEquals

class TextReportExporterTest {
    private val exporter = TextReportExporter()

    @Test
    fun `exports products as text report`() {
        val expected = """
            Отчет по товарам
            Всего товаров: 3

            #1 Mobil 1, Synthetic
              Тип: oil
              Цена, руб: 1125.0
              Вес, кг: 4.54
              Транспорт: PASSENGER
              Объем, л: 4.0

            #3 Michelin Primacy
              Тип: tire
              Цена, руб: 1350.0
              Вес, кг: 0.3
              Транспорт: PASSENGER
              Диаметр, дюймы: 16.0
              Сезон: SUMMER

            #5 Щетки
              Тип: accessory
              Цена, руб: 100.0
              Вес, кг: 0.1
              Транспорт: PASSENGER
              Материал: резина
        """.trimIndent() + "\n"

        assertEquals(expected, exporter.export(TestProducts.all))
    }

    @Test
    fun `exports empty list`() {
        assertEquals("Отчет по товарам\nВсего товаров: 0\n", exporter.export(emptyList()))
    }
}
