package com.example.smarthhome.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AutomationDB")
data class AutomationDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val status: String,
    val name: String
)