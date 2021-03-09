package edu.appstate.mccannsa.rememoir

import android.content.Intent
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
    private val db = Firebase.firestore
    private val TAG = "CreateTaskFragment"

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

        val task = hashMapOf(
            "name" to taskName,
            "dateTime" to taskDateTime,
            "checked" to false
        )

        db.collection("tasks")
            .add(task)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

         findNavController().navigate(R.id.action_createTaskFragment_to_navigation_tasks2)
    }

    private fun showDatePicker() {
        pickerDate.show(childFragmentManager, "datePicker")
    }

    private fun showTimePicker() {
        pickerTime.show(childFragmentManager, "timePicker")
    }
}