package edu.appstate.mccannsa.rememoir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.ui.tasks.TasksFragment

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var addTaskButton: Button
    private lateinit var nameText: String
    private lateinit var dateText: String
    private lateinit var timeText: String
    private val db = Firebase.firestore
    private val TAG = "CreateTaskActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        addTaskButton = findViewById<Button>(R.id.buttonAddTask)

        addTaskButton.setOnClickListener {

            nameText = findViewById<EditText>(R.id.editTextName).text.toString()
            dateText = findViewById<EditText>(R.id.editTextDate).text.toString()
            timeText = findViewById<EditText>(R.id.editTextTime).text.toString()

            val task = hashMapOf(
                "name" to nameText,
                "date" to dateText,
                "time" to timeText,
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

            val showMain = Intent(this, TasksFragment::class.java)
            startActivity(showMain)
        }
    }
}