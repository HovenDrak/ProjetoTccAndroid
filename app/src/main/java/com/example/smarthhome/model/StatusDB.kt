package com.example.smarthhome.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StatusDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val status: String
)