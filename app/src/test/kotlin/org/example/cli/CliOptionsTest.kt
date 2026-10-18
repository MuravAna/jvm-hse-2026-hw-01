package org.example.cli

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CliOptionsTest {
    @Test
    fun `uses defaults without arguments`() {
        assertEquals(CliOptions(format = null, input = "input.csv", output = null), CliOptions.parse(emptyArray()))
    }

    @Test
    fun `parses all arguments`() {
        assertEquals(
            CliOptions(format = "xml", input = "data.csv", output = "out.xml"),
            CliOptions.parse(arrayOf("--format=xml", "--input=data.csv", "--output=out.xml")),
        )
    }

    @Test
    fun `rejects malformed and unknown arguments`() {
        assertFailsWith<IllegalArgumentException> { CliOptions.parse(arrayOf("xml")) }
        assertFailsWith<IllegalArgumentException> { CliOptions.parse(arrayOf("--mode=fast")) }
    }
}
