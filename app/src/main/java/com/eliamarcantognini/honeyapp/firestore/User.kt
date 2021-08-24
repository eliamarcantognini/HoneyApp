package com.eliamarcantognini.honeyapp.firestore

data class User(
    val name: String? = null,
    val alias: String? = null,
    val level: Int? = null,
    val points: Int? = null,
    val scan: Int? = null
    )