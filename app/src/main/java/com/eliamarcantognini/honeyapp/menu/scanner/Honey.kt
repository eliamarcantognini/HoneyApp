package com.eliamarcantognini.honeyapp.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Honey(
    @SerialName("c")
    val controlChar: String? = null, // Control character
    @SerialName("fName")
    val firmName: String, // Firm name
    @SerialName("site")
    val site: String? = null, // Site url
    @SerialName("num")
    val telephoneNumber: String, // Telephone number
    @SerialName("addr")
    val address: String, // Address
    @SerialName("city")
    val city: String, // City
    @SerialName("cap")
    val cap: String, // CAP
    @SerialName("name")
    val honeyName: String, // Honey name
    @SerialName("desc")
    val honeyDescription: String // Honey description
) {

}