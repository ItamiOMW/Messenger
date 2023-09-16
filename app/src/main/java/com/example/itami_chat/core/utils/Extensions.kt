package com.example.itami_chat.core.utils


fun String.toIntList(): List<Int> {
    if (this == "[]") return emptyList()
    return this.substringAfter("[")
        .substringBefore("]")
        .split(",").map { it.toInt() }
}