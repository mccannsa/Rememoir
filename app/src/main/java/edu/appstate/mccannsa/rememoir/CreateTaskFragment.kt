package edu.appstate.mccannsa.rememoir

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.ui.tasks.TasksFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CreateTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTaskFragment : Fragment() {

    private lateinit var btnAddTask: Button
    private lateinit var etTaskName: EditText
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var pickerDate: DatePickerFragment
    private lateinit var pickerTime: TimePickerFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_create_task, container, false)

        etTaskName = root.findViewById(R.id.etTaskName)!!

        tvDate = root.findViewById(R.id.tvDate)!!
        pickerDate = DatePickerFragment(tvDate)
        tvDate.text = pickerDate.buildDateString()
        tvDate.setOnClickListener { showDatePicker() }

        tvTime = root.findViewById(R.id.tvTime)!!
        pickerTime = TimePickerFragment(tvTime)
        tvTime.text = pickerTime.buildTimeString()
        tvTime.setOnClickListener { showTimePicker() }

        btnAddTask = root.findViewById(R.id.btnAddTask)!!
        btnAddTask.setOnClickListener { addTask() }

        // Inflate the layout for this fragment
        return root
    }

    private fun addTask() {

        val taskName = etTaskName.text.toString()

        if (taskName.isBlank()) {

            val text = "Task name is blank!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext(), text, duration)
//            toast.setGravity(Gravity.BOTTOM or Gravity.LEFT, 0, 0)
            toast.show()
            return
        }

        val dateFormat = SimpleDateFormat("MM-dd-yyyy h:mm a")
        val dateText = "${pickerDate.pickMonth}-${pickerDate.pickDay}-${pickerDate.pickYear} ${pickerTime.buildTimeString()}"
        val taskDateTime = Timestamp(dateFormat.parse(dateText)!!)

        DataRepository.getInstance().addTask(taskName, taskDateTime, false)

        scheduleNotification(taskDateTime.toDate().time, taskName)

        findNavController().navigate(R.id.action_createTaskFragment_to_navigation_tasks2)
    }

    private fun showDatePicker() {
        pickerDate.show(childFragmentManager, "datePicker")
    }

    private fun showTimePicker() {
        pickerTime.show(childFragmentManager, "timePicker")
    }

    private fun scheduleNotification(time: Long, content: String) {
        val intent = Intent(requireContext(), ReminderReceiver::class.java)
        intent.putExtra("content", content)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(requireContext(),
            Calendar.getInstance().timeInMillis.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm: AlarmManager =
                requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
}