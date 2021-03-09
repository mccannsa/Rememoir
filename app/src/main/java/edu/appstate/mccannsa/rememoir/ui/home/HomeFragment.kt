package edu.appstate.mccannsa.rememoir.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Build a TimeStamp object with the current date to filter tasks
        val dateFormat = SimpleDateFormat("M-d-yyyy hh:mm a")
        val c = Calendar.getInstance()
        val currDateText = "${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}-${c.get(Calendar.YEAR)} 12:00 AM"
        val currTimestamp = Timestamp(dateFormat.parse(currDateText)!!)
        val nextDateText = "${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH) + 1}-${c.get(Calendar.YEAR)} 12:00 AM"
        val nextTimestamp = Timestamp(dateFormat.parse(nextDateText)!!)

        // Pull tasks from Firestore db
        db.collection("tasks")
                .orderBy("dateTime")
                .startAt(currTimestamp) // Get tasks for the current day
                .endBefore(nextTimestamp)
                .get()
                .addOnSuccessListener { result ->

                    val linearLayout = root.findViewById(R.id.lytHomeTasks) as LinearLayout // Displays task cards

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