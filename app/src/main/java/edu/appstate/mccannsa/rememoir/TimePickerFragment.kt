package edu.appstate.mccannsa.rememoir

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(tv: TextView) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val c = Calendar.getInstance()
    private var pickHour = c.get(Calendar.HOUR_OF_DAY)
    private var pickMin = c.get(Calendar.MINUTE)
    private var pickPeriod = c.get(Calendar.AM_PM)
    private val txtTime = tv

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Get the current time as a default value for the time picker
        pickHour = c.get(Calendar.HOUR_OF_DAY)
        pickMin = c.get(Calendar.MINUTE)
        pickPeriod = c.get(Calendar.AM_PM)

        txtTime.text = buildTimeString()

        return TimePickerDialog(requireContext(), this, pickHour, pickMin, false)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        pickHour = hourOfDay
        pickMin = minute
        txtTime.text = buildTimeString()
    }

    fun buildTimeString(): String {

        var str = ""

        pickPeriod = Calendar.AM
        if (pickHour > 12) {
            pickPeriod = Calendar.PM
            str += "${pickHour - 12}:"
        } else {
            str += "${pickHour}:"
        }

        if (pickMin < 10) {
            str += "0${pickMin}"
        } else {
            str += "${pickMin}"
        }

        if (pickPeriod == Calendar.PM) {
            str += " PM"
        } else {
            str += " AM"
        }

        return str
    }
}