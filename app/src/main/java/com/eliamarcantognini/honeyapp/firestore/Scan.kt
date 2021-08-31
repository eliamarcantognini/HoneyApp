package com.eliamarcantognini.honeyapp.firestore

data class Scan(
    val type: String? = null,
    val firm: String? = null,
    val desc: String? = null,
    val addr: String? = null,
    val city: String? = null,
    val cap: String? = null,
    val site: String? = null,
    val num: String? = null,
    var stars: Int? = null,
    val token: String? = null,
    var resID: Int? = null
)