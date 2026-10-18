package org.example.export

import org.example.model.Product
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame

class ExporterRegistryTest {
    private class CsvExporter : ProductExporter {
        override val name: String = "csv"
        override val extension: String = "csv"
        override fun export(products: List<Product>): String = products.joinToString("\n") { it.name }
    }

    @Test
    fun `default registry contains all formats`() {
        val registry = ExporterRegistry.withDefaults()

        assertEquals(listOf("json", "xml", "txt"), registry.available())
        assertIs<JsonExporter>(registry.get("json"))
        assertIs<XmlExporter>(registry.get("XML"))
        assertIs<TextReportExporter>(registry.get(" txt "))
    }

    @Test
    fun `returns null for unknown format`() {
        assertNull(ExporterRegistry.withDefaults().get("yaml"))
    }

    @Test
    fun `registers new format without changing existing code`() {
        val registry = ExporterRegistry.withDefaults()
        val csv = CsvExporter()

        registry.register(csv)

        assertSame(csv, registry.get("csv"))
        assertEquals(listOf("json", "xml", "txt", "csv"), registry.available())
        assertEquals("Mobil 1, Synthetic", registry.get("csv")?.export(listOf(TestProducts.oil)))
    }

    @Test
    fun `rejects duplicate format`() {
        val registry = ExporterRegistry.withDefaults()

        assertFailsWith<IllegalArgumentException> { registry.register(JsonExporter()) }
    }
}
