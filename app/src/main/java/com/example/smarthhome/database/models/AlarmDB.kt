package com.example.smarthhome.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlarmDB")
data class AlarmDB(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val status: String?,
    val showName: String?,
    val bypass: Int?
)