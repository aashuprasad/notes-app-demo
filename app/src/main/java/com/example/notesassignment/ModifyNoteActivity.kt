package com.example.notesassignment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesassignment.database.Note
import java.text.SimpleDateFormat
import java.util.*

// Defining ModifyNoteActivity class and inheriting from AppCompatActivity
class ModifyNoteActivity : AppCompatActivity() {

    // Declaring EditText, Button and NoteViewModel variables
    lateinit var noteTitleEdt: EditText
    lateinit var noteEdt: EditText
    lateinit var saveBtn: Button
    lateinit var viewModel: NoteViewModel
    var noteID = -1

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity layout
        setContentView(R.layout.activity_add_edit_note)

        // Initializing NoteViewModel and UI components
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        noteTitleEdt = findViewById(R.id.idEdtNoteName)
        noteEdt = findViewById(R.id.idEdtNoteDesc)
        saveBtn = findViewById(R.id.idBtn)

        // Fetching data from intent and displaying on UI
        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteId", -1)
            saveBtn.text = "Update Note"
            noteTitleEdt.setText(noteTitle)
            noteEdt.setText(noteDescription)
        } else {
            saveBtn.text = "Save Note"
        }

        // Setting click listener for save button
        saveBtn.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteEdt.text.toString()

            // When the user clicks on the saveBtn, the code checks if the noteType is "Edit" or "Add"
            // and validates if the note title and description fields are not empty.
            // If the noteType is "Edit" and the fields are not empty, the code updates the note with
            // the new data and displays a "Note Updated" toast message. If the noteType is "Add" and
            // the fields are not empty, the code creates a new note object with the data and displays
            // a "$noteTitle Added" toast message.
            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {

                    // Create new Note object with updated values and updating in database
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    val updatedNote = Note(noteTitle, noteDescription, currentDateAndTime)
                    updatedNote.id = noteID
                    viewModel.updateNote(updatedNote)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {

                    // Create new Note object and adding to the database
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    viewModel.addNote(Note(noteTitle, noteDescription, currentDateAndTime))
                    Toast.makeText(this, "$noteTitle Added", Toast.LENGTH_LONG).show()
                }
            }

            // Starting MainActivity and finishing this activity
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}