package org.example.export

import org.example.model.Oil
import org.example.model.VehicleType
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonExporterTest {
    private val exporter = JsonExporter()

    @Test
    fun `exports products as json array`() {
        val expected = """
            [
              {
                "id": 1,
                "type": "oil",
                "name": "Mobil 1, Synthetic",
                "price": 1125.0,
                "weight": 4.54,
                "vehicle": "PASSENGER",
                "volume": 4.0
              },
              {
                "id": 3,
                "type": "tire",
                "name": "Michelin Primacy",
                "price": 1350.0,
                "weight": 0.3,
                "vehicle": "PASSENGER",
                "diameter": 16.0,
                "season": "SUMMER"
              },
              {
                "id": 5,
                "type": "accessory",
                "name": "Щетки",
                "price": 100.0,
                "weight": 0.1,
                "vehicle": "PASSENGER",
                "material": "резина"
              }
            ]
        """.trimIndent() + "\n"

        assertEquals(expected, exporter.export(TestProducts.all))
    }

    @Test
    fun `exports empty list`() {
        assertEquals("[]\n", exporter.export(emptyList()))
    }

    @Test
    fun `escapes special characters`() {
        val product = Oil(1, "A \"B\" \\ C\n", 1.0, 1.0, VehicleType.PASSENGER, volume = null)

        val json = exporter.export(listOf(product))

        assertEquals(true, json.contains("\"name\": \"A \\\"B\\\" \\\\ C\\n\""))
    }
}
