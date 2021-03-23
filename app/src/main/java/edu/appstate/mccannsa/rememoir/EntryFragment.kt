package edu.appstate.mccannsa.rememoir

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.Timestamp

/**
 * A simple [Fragment] subclass.
 * Use the [EntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_entry, container, false)

        val tvTitle: TextView = root.findViewById(R.id.tvEntryTitle)
        val tvMood: TextView = root.findViewById(R.id.tvMood)
        val tvBody: TextView = root.findViewById(R.id.tvBody)
        val tvDate: TextView = root.findViewById(R.id.tvEntryDate)

        tvTitle.text = arguments?.getString("title")
        tvMood.text = arguments?.getString("mood")
        tvBody.text = arguments?.getString("body")
        tvDate.text = arguments?.getString("date")

        return root
    }
}