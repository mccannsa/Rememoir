package edu.appstate.mccannsa.rememoir.ui.tasks

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
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
            findNavController().navigate(R.id.action_navigation_tasks_to_createTaskFragment)
        }

        // pull tasks from db and populate view
        linearLayout = root.findViewById(R.id.taskLinearLayout)
        db.collection("tasks")
            .orderBy("dateTime")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    val name = document.data.get("name") as String
                    val taskDateTime = document.data.get("dateTime") as Timestamp
                    val checked = document.data.get("checked") as Boolean

                    val card = CardView(requireContext())
                    card.radius = 15f
                    card.setPadding(25, 25, 25, 25)

                    val cardLayout = LinearLayout(requireContext())
                    cardLayout.orientation = LinearLayout.VERTICAL

                    val checkBox = CheckBox(requireContext())
                    checkBox.text = name
                    checkBox.textSize = 20f
                    checkBox.isChecked = checked

                    checkBox.setOnClickListener {
                        document.reference.update("checked", checkBox.isChecked)
                    }
                    cardLayout.addView(checkBox)

                    val tv = TextView(requireContext())
                    tv.text = taskDateTime.toDate().toString()
                    cardLayout.addView(tv)

                    card.addView(cardLayout)
                    linearLayout.addView(card)
                }
            }
        return root
    }
}