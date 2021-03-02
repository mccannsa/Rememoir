package edu.appstate.mccannsa.rememoir

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(tv: TextView) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var pickYear = Int.MIN_VALUE
        private set
    var pickMonth = Int.MIN_VALUE
        private set
    var pickDay = Int.MIN_VALUE
        private set
    private val txtDate = tv

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Get the current date as a default value for the date picker
        val c = Calendar.getInstance()
        val pickYear = c.get(Calendar.YEAR)
        val pickMonth = c.get(Calendar.MONTH)
        val pickDay = c.get(Calendar.DAY_OF_MONTH)

        // Return a DatePickerDialog with the current date
        return DatePickerDialog(requireContext(), this, pickYear, pickMonth, pickDay)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        pickYear = year
        pickMonth = month + 1
        pickDay = dayOfMonth

        txtDate.text = "${getMonthName(pickMonth)} ${pickDay}, ${pickYear}"
    }

    private fun getMonthName(month: Int): String {

        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "INVALID"
        }
    }
}