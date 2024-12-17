package com.mbexample.alarmmanager.alarm

import com.mbexample.alarmmanager.data.sources.local.Alarm

interface AlarmScheduler {

    fun schedule(alarm: Alarm)
    fun cancel(alarm: Alarm)
}