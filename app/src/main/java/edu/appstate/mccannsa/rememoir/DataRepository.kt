package edu.appstate.mccannsa.rememoir

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class DataRepository {

    private val db = Firebase.firestore
    var entryList = mutableListOf<Entry>()
    var taskList = mutableListOf<Task>()

    companion object {

        private var repo: DataRepository? = null

        fun getInstance(): DataRepository {

            if (repo == null) {
                repo = DataRepository()
            }
            return repo!!
        }
    }

    fun addEntry(title: String, body: String, date: Date, mood: String) {

        val e = hashMapOf(
            "title" to title,
            "body" to body,
            "date" to date,
            "mood" to mood
        )

        db.collection("journals")
            .add(e)
            .addOnSuccessListener { result ->
                val id = result.id
                val entry = Entry(id, title, body, date, mood)
                entryList.add(entry)
            }
    }

    fun addTask(name: String, timestamp: Timestamp, checked: Boolean) {

        val t = hashMapOf(
            "name" to name,
            "dateTime" to timestamp,
            "checked" to checked
        )

        db.collection("tasks")
            .add(t)
            .addOnSuccessListener { result ->
                val id = result.id
                Log.d("Add Task: ", id)
                val task = Task(id, name, timestamp, checked)
                taskList.add(task)
            }
    }

    fun load() {

        // get tasks from db
        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    val id = document.id
                    val name = document.data["name"] as String
                    val timestamp = document.data["dateTime"] as Timestamp
                    val checked = document.data["checked"] as Boolean

                    val task = Task(id, name, timestamp, checked)
                    taskList.add(task)
                }
            }

        // get journal entries from db
        db.collection("journals")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    val id = document.id
                    val title = document.data["title"] as String
                    val mood = document.data["mood"] as String
                    val body = document.data["body"] as String
                    val timestamp = document.data["date"] as Timestamp
                    val date = timestamp.toDate()

                    val entry = Entry(id, title, body, date, mood)
                    entryList.add(entry)
                }
            }
    }

    fun updateTask(task: Task) {
        Log.d("updateTask", "trying to update")
        db.collection("tasks").document(task.id).update("checked", task.checked)
                .addOnSuccessListener { result ->
                    Log.d("updateTask: ", "Updated.")
                }
                .addOnFailureListener { result ->
                    Log.d("updateTask: ", "Failed update.")
                }
    }
}