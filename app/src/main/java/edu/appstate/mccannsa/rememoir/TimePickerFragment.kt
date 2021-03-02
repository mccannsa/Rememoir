package edu.appstate.mccannsa.rememoir

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(tv: TextView) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var pickHour = Int.MIN_VALUE
        private set
    var pickMin = Int.MIN_VALUE
        private set
    var pickPeriod = Int.MIN_VALUE
        private set
    var strTime: String? = ""
        private set
    private val txtTime = tv

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Get the current time as a default value for the time picker
        val c = Calendar.getInstance()
        pickHour = c.get(Calendar.HOUR_OF_DAY)
        pickMin = c.get(Calendar.MINUTE)
        pickPeriod = c.get(Calendar.AM_PM)

        return TimePickerDialog(requireContext(), this, pickHour, pickMin, false)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        strTime = ""
        pickHour = hourOfDay
        pickMin = minute

        pickPeriod = Calendar.AM
        if (pickHour > 12) {
            pickPeriod = Calendar.PM
            strTime += "${pickHour - 12}:"
        } else {
            strTime += "${pickHour}:"
        }

        if (pickMin < 10) {
            strTime += "0${pickMin}"
        } else {
            strTime += "${pickMin}"
        }

        if (pickPeriod == Calendar.PM) {
            strTime += " PM"
        } else {
            strTime += " AM"
        }

        txtTime.text = strTime
    }
}