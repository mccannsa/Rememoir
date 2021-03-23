package edu.appstate.mccannsa.rememoir.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.appstate.mccannsa.rememoir.EntryFragmentArgs
import edu.appstate.mccannsa.rememoir.EntryFragmentDirections
import edu.appstate.mccannsa.rememoir.R
import java.text.SimpleDateFormat
import java.util.*

class JournalFragment : Fragment() {

    private lateinit var notificationsViewModel: JournalViewModel
    private lateinit var linearLayout: LinearLayout
    private val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(JournalViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_journal, container, false)

        val createJournalButton = root.findViewById(R.id.btnCreateEntry) as Button
        createJournalButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_journal_to_createEntryFragment)
        }

        // Pull journal entries from Firestore db
        db.collection("journals")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->

                    linearLayout = root.findViewById(R.id.lytJournalList) // Displays entry cards

                    for (document in result) {

                        val title = document.data.get("title") as String
                        val date = document.data.get("date") as Timestamp
                        val mood = document.data.get("mood") as String
                        val body = document.data.get("body") as String

                        // Create card to display entry info
                        val card = CardView(requireContext())

                        val cardLayout = LinearLayout(requireContext())
                        cardLayout.orientation = LinearLayout.VERTICAL

                        val tvTitle = TextView(requireContext())
                        tvTitle.text = "${title} ${mood}"
                        cardLayout.addView(tvTitle)

                        val tvDate = TextView(requireContext())
                        val dateStr = android.text.format.DateFormat.format(
                            "MMMM d, yyyy", date.toDate()).toString()
                        tvDate.text = dateStr
                        cardLayout.addView(tvDate)

                        card.addView(cardLayout)
                        card.setOnClickListener { showEntry(title, mood, body, dateStr) }
                        linearLayout.addView(card)
                    }
                }
        return root
    }

    private fun showEntry(title: String, mood: String, body: String, date: String) {
        val bundle = bundleOf(
            "title" to title,
            "mood" to mood,
            "body" to body,
            "date" to date
        )

        findNavController().navigate(R.id.action_navigation_journal_to_entryFragment, bundle)
    }
}