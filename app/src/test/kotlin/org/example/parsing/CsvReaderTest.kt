package org.example.parsing

import kotlin.test.Test
import kotlin.test.assertEquals

class CsvReaderTest {
    @Test
    fun `skips header and blank lines and numbers data lines from one`() {
        val rows = CsvReader.parseLines(listOf("id,name", "1,a", "", "3,c\r"))

        assertEquals(listOf(CsvRow(1, "1,a"), CsvRow(3, "3,c")), rows)
    }
}
