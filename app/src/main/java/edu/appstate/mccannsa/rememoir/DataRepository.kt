package edu.appstate.mccannsa.rememoir

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * DateRepository - Stores tasks and journal entries and updates Firestore.
 *                  Uses Singleton design to ensure only one local repository.
 */
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

    /**
     * addEntry - Adds a journal entry to both entryList and Firestore.
     *
     *            Uses the parameters to first add an entry to Firestore.
     *            Then it creates an Entry object with the document ID
     *            of the entry just added to Firestore and adds it to
     *            entryList.
     *
     * @param title The title of the entry
     * @param body The body of the entry
     * @param date The date of the entry
     * @param mood The mood of the entry
     */
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

    /**
     * addTask - Adds a task to both taskList and Firestore.
     *
     *           Uses the parameters to first add a task to Firestore.
     *           Then it creates a Task object with the document ID
     *           of the task just added to Firestore and adds it to
     *           taskList.
     *
     * @param name The task name
     * @param timestamp The date and time of the task
     * @param checked Whether or not the task is checked
     */
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

    /**
     * load - Retrieves tasks and journal entries from Firestore to store
     *        in DataRepository.
     *
     *        Task and journal documents are converted into Task and Entry
     *        objects and added to taskList and entryList, respectively.
     */
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

    /**
     * updateTask - Updates a task in Firestore. Currently only
     *              updates the checked status of a task.
     *
     * @param task The task being updated
     */
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