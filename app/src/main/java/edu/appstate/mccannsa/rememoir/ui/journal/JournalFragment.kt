package edu.appstate.mccannsa.rememoir.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.appstate.mccannsa.rememoir.R

class JournalFragment : Fragment() {

    private lateinit var notificationsViewModel: JournalViewModel

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
        return root
    }
}