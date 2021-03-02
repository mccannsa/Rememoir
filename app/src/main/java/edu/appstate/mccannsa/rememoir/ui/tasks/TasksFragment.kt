package edu.appstate.mccannsa.rememoir.ui.tasks

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.CreateTaskActivity
import edu.appstate.mccannsa.rememoir.R
import java.text.SimpleDateFormat
import java.util.*

class TasksFragment : Fragment() {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var linearLayout: LinearLayout
    private lateinit var createTaskButton: Button
    private val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tasksViewModel =
                ViewModelProvider(this).get(TasksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)

        createTaskButton = root.findViewById(R.id.buttonCreateTask)
        createTaskButton.setOnClickListener {
            val showCreateTask = Intent(activity, CreateTaskActivity::class.java)
            startActivity(showCreateTask)
        }

        // pull tasks from db and populate view
        linearLayout = root.findViewById(R.id.taskLinearLayout)
        db.collection("tasks")
                .get()
                .addOnSuccessListener { result ->

                    val dateFormat = SimpleDateFormat("MMMMM dd h:mm a")

                    for (document in result) {

                        val name = document.data.get("name") as String
                        val taskDateTime = document.data.get("dateTime") as Timestamp
                        val checked = document.data.get("checked") as Boolean

                        val card = CardView(requireContext())
                        card.radius = 15f
                        card.setPadding(25, 25, 25, 25)
                        card.setCardBackgroundColor(Color.LTGRAY)

                        val checkBox = CheckBox(requireContext())
                        checkBox.text = name
                        checkBox.textSize = 20f
                        checkBox.isChecked = checked

                        checkBox.setOnClickListener {
                            document.reference.update("checked", checkBox.isChecked)
                        }

                        val tv = TextView(requireContext())
                        tv.text = taskDateTime.toDate().toString()
                        tv.setPadding(50)

                        card.addView(checkBox)
                        card.addView(tv)
                        linearLayout.addView(card)
                    }
                }
        return root
    }
}