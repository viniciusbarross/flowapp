package com.example.flowapp.model
import java.io.Serializable

data class Transaction(
    val id: Long =0,
    val type: String,
    val detail: String,
    val value: Double,
    val date: String
) : Serializable
