package com.mbexample.alarmmanager.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Alarm::class],
    version = 1
)
abstract class AlarmDatabase: RoomDatabase() {
    abstract val getAlarmDao: AlarmDao

}