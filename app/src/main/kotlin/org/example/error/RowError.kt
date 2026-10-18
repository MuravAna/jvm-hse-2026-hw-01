package org.example.error

enum class ErrorKind {
    PARSING,
    VALIDATION
}

data class RowError(val line: Int, val kind: ErrorKind, val message: String)

class ParseException(message: String) : RuntimeException(message)
