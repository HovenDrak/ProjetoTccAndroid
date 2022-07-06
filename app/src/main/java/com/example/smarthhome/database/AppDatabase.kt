package com.example.smarthhome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smarthhome.constants.Constants.NAME_DATABASE
import com.example.smarthhome.database.dao.StatusDAO
import com.example.smarthhome.model.StatusDB

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
                NAME_DATABASE
            ).build()

            return db
        }
    }
}