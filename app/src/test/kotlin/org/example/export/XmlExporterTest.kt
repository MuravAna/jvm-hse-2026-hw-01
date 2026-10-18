package org.example.export

import org.example.model.Accessory
import org.example.model.VehicleType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class XmlExporterTest {
    private val exporter = XmlExporter()

    @Test
    fun `exports products as xml`() {
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <products>
              <product>
                <id>1</id>
                <type>oil</type>
                <name>Mobil 1, Synthetic</name>
                <price>1125.0</price>
                <weight>4.54</weight>
                <vehicle>PASSENGER</vehicle>
                <volume>4.0</volume>
              </product>
              <product>
                <id>3</id>
                <type>tire</type>
                <name>Michelin Primacy</name>
                <price>1350.0</price>
                <weight>0.3</weight>
                <vehicle>PASSENGER</vehicle>
                <diameter>16.0</diameter>
                <season>SUMMER</season>
              </product>
              <product>
                <id>5</id>
                <type>accessory</type>
                <name>Щетки</name>
                <price>100.0</price>
                <weight>0.1</weight>
                <vehicle>PASSENGER</vehicle>
                <material>резина</material>
              </product>
            </products>
        """.trimIndent() + "\n"

        assertEquals(expected, exporter.export(TestProducts.all))
    }

    @Test
    fun `exports empty list`() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<products>\n</products>\n", exporter.export(emptyList()))
    }

    @Test
    fun `escapes special characters`() {
        val product = Accessory(1, "<A & 'B'>", 1.0, 1.0, VehicleType.PASSENGER, material = "\"x\"", color = null)

        val xml = exporter.export(listOf(product))

        assertTrue(xml.contains("<name>&lt;A &amp; &apos;B&apos;&gt;</name>"))
        assertTrue(xml.contains("<material>&quot;x&quot;</material>"))
    }
}
