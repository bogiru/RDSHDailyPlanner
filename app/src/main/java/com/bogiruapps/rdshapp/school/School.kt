package com.bogiruapps.rdshapp.school

data class School (
    val name: String = "",
    val id: String = ""
)  {

    override fun toString(): String = name

}