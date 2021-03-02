package edu.appstate.mccannsa.rememoir

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.ui.tasks.TasksFragment
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var btnAddTask: Button
    private lateinit var txtName: EditText
    private lateinit var txtDate: TextView
    private lateinit var txtTime: TextView
    private lateinit var pickerDate: DatePickerFragment
    private lateinit var pickerTime: TimePickerFragment
    private val db = Firebase.firestore
    private val TAG = "CreateTaskActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        txtName = findViewById(R.id.text_taskName)

        txtDate = findViewById(R.id.text_date)
        txtDate.setOnClickListener { showDatePicker() }
        pickerDate = DatePickerFragment(txtDate)

        txtTime = findViewById(R.id.text_time)
        txtTime.setOnClickListener { showTimePicker() }
        pickerTime = TimePickerFragment(txtTime)

        btnAddTask = findViewById(R.id.button_addTask)
        btnAddTask.setOnClickListener { addTask() }
    }

    private fun addTask() {

        val taskName = txtName.text.toString()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy h:mm a")
        val dateText = "${pickerDate.pickMonth}-${pickerDate.pickDay}-${pickerDate.pickYear} ${pickerTime.strTime}"
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

        val showMain = Intent(this, MainActivity::class.java)
        startActivity(showMain)
    }

    private fun showDatePicker() {
        pickerDate.show(supportFragmentManager, "datePicker")
    }

    private fun showTimePicker() {
        pickerTime.show(supportFragmentManager, "timePicker")
    }
}