package org.example.model

enum class Season {
    SUMMER,
    WINTER,
    ALL_SEASON;

    companion object {
        fun fromName(name: String): Season? = entries.firstOrNull { it.name == name }
    }
}
