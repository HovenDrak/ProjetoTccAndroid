package com.example.smarthhome.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smarthhome.database.dao.StatusDAO
import com.example.smarthhome.model.StatusDB

private const val NOME_BANCO_DE_DADOS = "smarthome.db"

@Database(entities = [StatusDB::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract val statusDAO: StatusDAO

    companion object{

        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {

            if (::db.isInitialized) return db

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                NOME_BANCO_DE_DADOS
            ).build()

            return db
        }
    }
}