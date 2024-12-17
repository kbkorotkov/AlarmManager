package com.mbexample.alarmmanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mbexample.alarmmanager.data.repository.AlarmRepository
import com.mbexample.alarmmanager.data.sources.local.Alarm
import com.mbexample.alarmmanager.ui.AlarmItemAdapter
import com.mbexample.alarmmanager.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmActivityViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
):ViewModel(),AlarmItemAdapter.AlarmItemDismissClickListener {

    private val _onAlarmItemSwipe = MutableLiveData<Event<Alarm>>()
    val onAlarmItemSwipe: LiveData<Event<Alarm>> = _onAlarmItemSwipe

    private val _onAlarmDismissItemClick = MutableLiveData<Event<Unit>>()
    val onAlarmDismissItemClick: LiveData<Event<Unit>> = _onAlarmDismissItemClick

    fun insertAlarm(alarm: Alarm)= viewModelScope.launch{
        alarmRepository.insertAlarm(alarm)
    }

    fun deleteAlarmItemById(alarmId: Long)= viewModelScope.launch{
        alarmRepository.deleteAlarmById(alarmId)
    }

    val getAllAlarm = alarmRepository.getAllAlarm().asLiveData()

    override fun onAlarmItemDismissClick(alarm: Event<Unit>) {
        _onAlarmDismissItemClick.value = Event(Unit)
    }

    override fun onAlarmItemSwipe(alarm: Alarm) {
        _onAlarmItemSwipe.value = Event(alarm)
    }
}