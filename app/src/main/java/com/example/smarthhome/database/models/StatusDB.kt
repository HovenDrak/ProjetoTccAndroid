package com.example.smarthhome.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StatusDB")
data class StatusDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val status: String
)