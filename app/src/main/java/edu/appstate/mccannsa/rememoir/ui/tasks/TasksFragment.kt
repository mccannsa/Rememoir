package edu.appstate.mccannsa.rememoir.ui.tasks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.CreateTaskActivity
import edu.appstate.mccannsa.rememoir.R

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
                    for (document in result) {
                        val name = document.data.get("name") as String
                        val checked = document.data.get("checked") as Boolean

                        val card = CardView(requireContext())
                        card.radius = 15f
                        card.setPadding(25, 25, 25, 25)

                        val checkBox = CheckBox(requireContext())
                        checkBox.text = name
                        checkBox.isChecked = checked

                        checkBox.setOnClickListener {
                            document.reference.update("checked", checkBox.isChecked)
                        }

                        card.addView(checkBox)
                        linearLayout.addView(card)
                    }
                }
        return root
    }
}