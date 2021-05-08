package edu.appstate.mccannsa.rememoir

import java.util.Date

class Entry(val i: String, val t: String, val b: String, val d: Date, val m: String) {
    var id = i
    var title = t
    var body = b
    var date = d
    var mood = m
}