package com.mbexample.alarmmanager.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.mbexample.alarmmanager.R
import com.mbexample.alarmmanager.alarm.AlarmSchedulerImpl
import com.mbexample.alarmmanager.data.sources.local.Alarm
import com.mbexample.alarmmanager.databinding.ActivityAlarmBinding
import com.mbexample.alarmmanager.ui.AlarmItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private lateinit var adapter: AlarmItemAdapter
    private val viewModel: AlarmActivityViewModel by viewModels()
    private lateinit var alarmSchedulerImpl: AlarmSchedulerImpl
    private var alarm: Alarm? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        adapter = AlarmItemAdapter(viewModel)
        adapter.touchHelper.attachToRecyclerView(binding.alarmList)
        alarmSchedulerImpl = AlarmSchedulerImpl(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.alarmList.layoutManager = LinearLayoutManager(this)

        viewModel.getAllAlarm.observe(this) {
            adapter.submitList(it)
        }

        viewModel.onAlarmItemSwipe.observe(this){
            it.getContentIfNotHandled()?.let {alarmItem ->
                alarm?.let(alarmSchedulerImpl::cancel)
                viewModel.deleteAlarmItemById(alarmItem.id)
            }
        }

        viewModel.onAlarmDismissItemClick.observe(this) { onDismissClick ->
            onDismissClick.getContentIfNotHandled()?.let {
                alarm?.let(alarmSchedulerImpl::cancel)
            }
        }

        binding.alarmList.adapter = adapter

        binding.createAlarm.setOnClickListener {

            if (isPermissionGranted()) {
                openDialog()
            } else {
                activityResultLauncher.launch(
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissions ->
        if (permissions) {
            openDialog()
        } else {
            showEducationalDialog()
        }
    }

    private fun openDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        val view = layoutInflater.inflate(R.layout.dialog_create_alarm, null)
        builder.setCancelable(false)
        builder.setView(view).setPositiveButton(
            R.string.pick_time
        ) { dialog, _ ->

            val etTitle = view.findViewById<EditText>(R.id.etTitle)
            val etMsg = view.findViewById<EditText>(R.id.etDesc)

            val title = etTitle.text.toString()
            val desc = etMsg.text.toString()
            if (title.isBlank() || desc.isBlank()) {
                Toast.makeText(this, R.string.msg_missing_field, Toast.LENGTH_SHORT).show()
            } else {
                openTimePicker(etTitle.text.toString(), etMsg.text.toString())
                dialog.dismiss()
            }


        }.setNegativeButton(
            R.string.cancel
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()

    }

    private fun showEducationalDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_denied)
            .setMessage(R.string.request_msg)
            .setNegativeButton(R.string.close) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton(R.string.settings) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
                dialog.dismiss()
            }
            .setCancelable(false)
        dialog.show()
    }


    private fun openTimePicker(title: String, message: String) {

        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.HOUR, 1)
        calendar.set(Calendar.SECOND, 0)

        val dialog = MaterialTimePicker.Builder()
            .setTimeFormat(if (DateFormat.is24HourFormat(this)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setTitleText(R.string.pick_time)
            .setHour(calendar.get(Calendar.HOUR))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setInputMode(INPUT_MODE_CLOCK)
            .setPositiveButtonText(R.string.ok)
            .setNegativeButtonText(R.string.cancel)
            .build()

        dialog.addOnPositiveButtonClickListener {
            createAlarm(title, message, dialog.hour, dialog.minute)
        }
        dialog.addOnNegativeButtonClickListener {
            dialog.dismiss()
        }

        dialog.show(supportFragmentManager, "TimePicker")

    }

    private fun createAlarm(title: String, message: String, hour: Int, min: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, 0)
        alarm = Alarm(
            0L,
            title,
            message,
            calendar.timeInMillis
        )

        alarm?.let(alarmSchedulerImpl::schedule)

        viewModel.insertAlarm(
            alarm!!
        )

    }

    private fun isPermissionGranted() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

}