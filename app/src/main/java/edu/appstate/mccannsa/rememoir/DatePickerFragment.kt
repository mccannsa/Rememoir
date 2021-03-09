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

    private val c = Calendar.getInstance()
    var pickYear = c.get(Calendar.YEAR)
        private set
    var pickMonth = c.get(Calendar.MONTH) + 1
        private set
    var pickDay = c.get(Calendar.DAY_OF_MONTH)
        private set
    private val txtDate = tv

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Get the current date as a default value for the date picker
        pickYear = c.get(Calendar.YEAR)
        pickMonth = c.get(Calendar.MONTH) + 1
        pickDay = c.get(Calendar.DAY_OF_MONTH)

        txtDate.text = buildDateString()

        // Return a DatePickerDialog with the current date
        return DatePickerDialog(requireContext(), this, pickYear, pickMonth - 1, pickDay)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        pickYear = year
        pickMonth = month + 1
        pickDay = dayOfMonth

        txtDate.text = buildDateString()
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

    fun buildDateString(): String {

        return "${getMonthName(pickMonth)} ${pickDay}, ${pickYear}"
    }
}