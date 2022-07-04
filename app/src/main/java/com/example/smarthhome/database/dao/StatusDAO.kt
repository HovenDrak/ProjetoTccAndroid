package com.example.smarthhome.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smarthhome.model.StatusDB

@Dao
interface StatusDAO {

    @Query("SELECT * FROM statusdb")
    fun consultAllState(): MutableList<StatusDB>

    @Query("UPDATE statusdb SET status = :state WHERE id == :id")
    fun updateState(state: String, id: Int)

}