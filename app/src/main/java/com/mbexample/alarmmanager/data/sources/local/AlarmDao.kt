package com.mbexample.alarmmanager.data.sources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Upsert
    suspend fun upsertAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM Alarm WHERE id = :alarmId ")
    fun getAlarmById(alarmId: Long): Alarm

    @Query("SELECT * FROM Alarm ORDER BY id DESC")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Query("DELETE FROM Alarm WHERE id = :alarmId")
    suspend fun deleteAlarmById(alarmId: Long)

}