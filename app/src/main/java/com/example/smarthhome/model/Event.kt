package com.example.smarthhome.model

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

data class Event(
    val id: Int,
    val descricao: String,
    val user: String,
    val date: String
)
