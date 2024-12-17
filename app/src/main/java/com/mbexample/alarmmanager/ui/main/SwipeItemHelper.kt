package com.mbexample.alarmmanager.ui.main

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mbexample.alarmmanager.ui.AlarmItemAdapter

class SwipeItemHelper(
    private val listener: AlarmItemAdapter.AlarmItemDismissClickListener? = null,
    private val adapter: AlarmItemAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener?.onAlarmItemSwipe(adapter.currentList[viewHolder.bindingAdapterPosition])
    }
}