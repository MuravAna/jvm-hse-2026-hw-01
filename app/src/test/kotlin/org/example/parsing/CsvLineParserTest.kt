package org.example.parsing

import org.example.error.ParseException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CsvLineParserTest {
    @Test
    fun `parses line without quotes`() {
        assertEquals(
            listOf("2", "Castrol Magnatec", "oil", "300.5руб", "1.2kg", "PASSENGER", "volume=1"),
            CsvLineParser.parse("2,Castrol Magnatec,oil,300.5руб,1.2kg,PASSENGER,volume=1"),
        )
    }

    @Test
    fun `keeps commas inside quoted field`() {
        assertEquals(
            listOf("1", "Mobil 1, Synthetic", "oil"),
            CsvLineParser.parse("1,\"Mobil 1, Synthetic\",oil"),
        )
    }

    @Test
    fun `unescapes doubled quotes`() {
        assertEquals(
            listOf("1", "Масло \"Лукойл\", 5л"),
            CsvLineParser.parse("1,\"Масло \"\"Лукойл\"\", 5л\""),
        )
    }

    @Test
    fun `keeps empty fields`() {
        assertEquals(listOf("8", "", "tire", ""), CsvLineParser.parse("8,,tire,"))
    }

    @Test
    fun `fails on unclosed quote`() {
        assertFailsWith<ParseException> { CsvLineParser.parse("1,\"Mobil,oil") }
    }

    @Test
    fun `fails on text after closing quote`() {
        assertFailsWith<ParseException> { CsvLineParser.parse("1,\"Mobil\"x,oil") }
    }
}
