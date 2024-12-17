package com.mbexample.alarmmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mbexample.alarmmanager.R
import com.mbexample.alarmmanager.data.sources.local.Alarm
import com.mbexample.alarmmanager.databinding.AlarmListItemBinding
import com.mbexample.alarmmanager.ui.main.SwipeItemHelper
import com.mbexample.alarmmanager.utils.Event
import java.text.DateFormat

class AlarmItemAdapter (
    private val alarmItemDismissClickListener: AlarmItemDismissClickListener
): ListAdapter<Alarm, AlarmItemAdapter.HomeViewHolder>(diffCallBack) {

    inner class HomeViewHolder(private val binding: AlarmListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.apply {
                tvTitle.isVisible = alarm.title.isNotBlank()
                tvDesc.isVisible = alarm.message.isNotBlank()
                tvTitle.text = alarm.title
                tvDesc.text = alarm.message
                val df: DateFormat = DateFormat.getTimeInstance()
                tvAlarmTime.text = df.format(alarm.scheduleAt)
                btnDismiss.setOnClickListener {
                    alarmItemDismissClickListener.onAlarmItemDismissClick(Event(Unit))
                    tvAlarmTime.isEnabled = false
                }
            }
        }
    }

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<Alarm>() {
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            AlarmListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    val touchHelper = ItemTouchHelper(SwipeItemHelper(this.alarmItemDismissClickListener, this))


    interface AlarmItemDismissClickListener {
        fun onAlarmItemDismissClick(alarm: Event<Unit>)
        fun onAlarmItemSwipe(alarm: Alarm)
    }

}


