package com.mbexample.alarmmanager.data.repository

import com.mbexample.alarmmanager.data.sources.local.Alarm
import com.mbexample.alarmmanager.data.sources.local.AlarmDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** [AlarmRepository] implementation*/

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDatabase: AlarmDatabase
): AlarmRepository {

    override suspend fun insertAlarm(alarm: Alarm) {
        alarmDatabase.getAlarmDao.insertAlarm(alarm)
    }

    override suspend fun upsertAlarm(alarm: Alarm) {
        alarmDatabase.getAlarmDao.upsertAlarm(alarm)
    }

    override fun getAlarmById(alarmId: Long): Alarm {
       return alarmDatabase.getAlarmDao.getAlarmById(alarmId)
    }

    override fun getAllAlarm(): Flow<List<Alarm>> {
        return alarmDatabase.getAlarmDao.getAllAlarms()
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDatabase.getAlarmDao.deleteAlarm(alarm)
    }

    override suspend fun deleteAlarmById(alarmId:Long) {
        alarmDatabase.getAlarmDao.deleteAlarmById(alarmId)
    }

}