package com.example.smarthhome.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.smarthhome.database.models.AlarmDB
import com.example.smarthhome.database.models.AutomationDB

@Dao
interface StatusDAO {

    @Query("SELECT * FROM automationdb")
    fun consultAllState(): MutableList<AutomationDB>

    @Query("UPDATE automationdb SET status = :state WHERE id == :id")
    fun updateState(state: String, id: Int)

    @Query("INSERT INTO automationdb VALUES(:id, :status, :name)")
    fun insertAutomationDB(id: Int, status: String, name: String)

    @Query("DELETE FROM automationdb")
    fun deleteAutomationDB()


    @Query("INSERT INTO alarmdb VALUES(:id, :name, :status, :showName, :bypass)")
    fun insertStatusAlarm(id: Int, name: String, status: String, showName: String, bypass: Int)

    @Query("UPDATE alarmdb SET status = :state, bypass = :bypass WHERE name == :name")
    fun updateStatusAlarm(state: String, bypass: Int, name: String)

    @Query("UPDATE alarmdb SET status = :state, bypass = :bypass WHERE id == :id")
    fun updateStatusAlarm(state: String, bypass: Int, id: Int)

    @Query("UPDATE alarmdb SET bypass =:bypass WHERE id == :id")
    fun updateStateBypass(bypass: Int, id: Int)

    @Query("SELECT EXISTS(SELECT * FROM alarmdb)")
    fun isExists(): Boolean

    @Query("SELECT * FROM alarmdb")
    fun getAllAlarmStatus(): List<AlarmDB>

    @Query("SELECT id FROM alarmdb WHERE bypass == 1")
    fun getAllSensorsBypass(): List<Int>

    @Query("SELECT * FROM alarmdb WHERE id == :id")
    fun getStatusAlarm(id: Int): AlarmDB

    @Query("DELETE FROM alarmdb")
    fun deleteAlarmDB()

    @Query("DELETE FROM alarmdb WHERE id == :id")
    fun deleteStatusAlarm(id: Int)
}