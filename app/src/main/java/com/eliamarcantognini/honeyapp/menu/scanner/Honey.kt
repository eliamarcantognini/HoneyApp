package com.eliamarcantognini.honeyapp.menu.scanner

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Honey(
    @SerialName("c")
    val token: String, // Control character
    @SerialName("f")
    val firmName: String, // Firm name
    @SerialName("s")
    val site: String? = null, // Site url
    @SerialName("n")
    val telephoneNumber: String, // Telephone number
    @SerialName("a")
    val address: String, // Address
    @SerialName("p")
    val city: String, // City
    @SerialName("C")
    val cap: String, // CAP
    @SerialName("h")
    val type: Int, // Honey type
    @SerialName("d")
    val description: String // Honey description
) {

}