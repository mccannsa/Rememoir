package edu.appstate.mccannsa.rememoir

import com.google.firebase.Timestamp

class Task(val i: String, val n: String, val t: Timestamp, var c: Boolean) {
    var id = i
    var name = n
    var timestamp = t
    var checked = c
}