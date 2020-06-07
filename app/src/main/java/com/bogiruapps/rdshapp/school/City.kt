package com.bogiruapps.rdshapp.school

data class City(
    val id: String = "",
    val name: String = ""
) {

    override fun toString(): String = name

}