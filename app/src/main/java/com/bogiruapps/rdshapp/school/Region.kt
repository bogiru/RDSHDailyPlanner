package com.bogiruapps.rdshapp.school

data class Region(
    val id: String = "",
    val name: String = ""
) {
    override fun toString(): String {
        return name
    }
}