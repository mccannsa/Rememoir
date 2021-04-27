package edu.appstate.mccannsa.rememoir

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CreateEntryFragment : Fragment() {

    private lateinit var spMood: Spinner
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var btnAddEntry: Button
    private val db = Firebase.firestore
    private val TAG = "CreateEntryFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_create_entry, container, false)

        spMood = root.findViewById(R.id.spMood)
        ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.mood_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spMood.adapter = adapter
        }

        etTitle = root.findViewById(R.id.etEntryTitle)
        etBody = root.findViewById(R.id.etBody)
        etBody.setOnFocusChangeListener { v, hasFocus ->
//            if (v.id != R.id.etEntryTitle && !hasFocus) {
//                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(v.windowToken, 0)
//            }
        }

        btnAddEntry = root.findViewById(R.id.btnAddEntry)!!
        btnAddEntry.setOnClickListener { addEntry() }

        // Inflate the layout for this fragment
        return root
    }

    private fun addEntry() {

        val title = etTitle.text.toString()

        if (title.isBlank()) {

            val text = "Entry title is blank!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext(), text, duration)
            toast.show()
            return
        }

        val mood = spMood.selectedItem.toString()

        val body = etBody.text.toString()

        if (body.isBlank()) {
            val text = "Entry body is blank!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext(), text, duration)
            toast.show()
            return
        }

        val c = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("M-d-yyyy h:mm a")
        val dateText = "${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}-${c.get(Calendar.YEAR)} 12:00 AM"
        val journalDate = Timestamp(dateFormat.parse(dateText)!!)

        // check if a journal entry already exists for today
        var exists = false
        for (entry in DataRepository.getInstance().entryList) {
            if (entry.date == journalDate.toDate()) {
                exists = true
                break
            }
        }

        if (!exists) {
            DataRepository.getInstance().addEntry(title, body, journalDate.toDate(), mood)
        } else {
            val text = "Entry already exists for today!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext(), text, duration)
            toast.show()
        }

        findNavController().navigate(R.id.action_createEntryFragment_to_navigation_journal)
    }
}