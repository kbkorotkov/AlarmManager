package com.mbexample.alarmmanager.data.repository

import com.mbexample.alarmmanager.data.sources.local.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun upsertAlarm(alarm: Alarm)

    fun getAlarmById(alarmId: Long): Alarm

    fun getAllAlarm(): Flow<List<Alarm>>

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarmById(alarmId: Long)

}