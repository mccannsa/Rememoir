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
import java.text.DateFormat
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

        // Build a TimeStamp object with the current date to filter tasks
        val dateFormat = SimpleDateFormat("M-d-yyyy hh:mm a")
        val c = Calendar.getInstance()
        val dateText = "${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}-${c.get(Calendar.YEAR)} 12:00 AM"
        val timestamp = Timestamp(dateFormat.parse(dateText)!!)

        // Pull tasks from Firestore db
        db.collection("tasks")
                .orderBy("dateTime")
                .startAt(timestamp) // Get tasks from the current date and onward
                .get()
                .addOnSuccessListener { result ->

                    linearLayout = root.findViewById(R.id.taskLinearLayout) // Displays task cards

                    for (document in result) {

                        val name = document.data.get("name") as String
                        val taskDateTime = document.data.get("dateTime") as Timestamp
                        val checked = document.data.get("checked") as Boolean

                        // Create card to display task info
                        val card = CardView(requireContext())

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
                        tv.text = android.text.format.DateFormat.format(
                                "MMMM d, h:mm a", taskDateTime.toDate())
                        tv.setPadding(checkBox.compoundPaddingLeft, 0, 0, 0)
                        cardLayout.addView(tv)

                        card.addView(cardLayout)
                        linearLayout.addView(card)
                    }
                }
        return root
    }
}