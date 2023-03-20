package com.example.notesassignment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesassignment.database.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

//MainActivity extends AppCompatActivity, and implements two interfaces:
//NoteClickInterface and NoteClickDeleteInterface.
class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {

    //viewModel, notesRecyclerView, and floatingActionButton are declared using
    //lateinit, which means they will be initialized later in the code.
    lateinit var viewModel: NoteViewModel
    lateinit var notesRecyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton


    //onCreate() function is the entry point for this activity, and is called when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView() function sets the layout for the activity,
        //which is defined in a separate XML file.
        setContentView(R.layout.activity_main)


        //notesRecyclerView and floatingActionButton variables are initialized using
        //findViewById() to reference the corresponding views defined in the layout XML.
        notesRecyclerView = findViewById(R.id.notesRV)
        floatingActionButton = findViewById(R.id.idFAB)

        //notesRecyclerView is set up with a LinearLayoutManager,
        //which controls how the notes will be displayed in the RecyclerView.
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

        //NoteRVAdapter is created, passing in three arguments: the MainActivity itself,
        //and the two interfaces implemented by the MainActivity.
        val noteRVAdapter = NoteRVAdapter(this, this, this)

        //NoteRVAdapter is then set as the adapter for the RecyclerView.
        notesRecyclerView.adapter = noteRVAdapter

        //ViewModelProvider is created, passing in the MainActivity, and an AndroidViewModelFactory
        //instance from the application. This will give the MainActivity access to a NoteViewModel,
        //which is responsible for managing the data for the app.
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(NoteViewModel::class.java)

        //LiveData object is obtained from the NoteViewModel using the getAllNotes() function.
        //This LiveData object observes changes to the list of notes, and updates the NoteRVAdapter accordingly.
        viewModel.getAllNotes().observe(this) { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        }

        //floatingActionButton is set up with an OnClickListener that creates an Intent to start
        //ModifyNoteActivity, and then starts that activity. The MainActivity is finished, meaning
        //it will be removed from the activity stack.
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ModifyNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    //onNoteClick() function is called when a note is clicked in the RecyclerView. It creates an
    //Intent to start a ModifyNoteActivity, passing in some extra data to indicate that the note
    //should be edited, and then starts that activity. The MainActivity is finished.
    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, ModifyNoteActivity::class.java).apply {
            putExtra("noteType", "Edit")
            putExtra("noteTitle", note.title)
            putExtra("noteDescription", note.description)
            putExtra("noteId", note.id)
            startActivity(this)
        }
        this.finish()
    }

    //onDeleteIconClick() function is called when the delete icon is clicked for a note.
    //It tells the NoteViewModel to delete the note, and then displays a Toast message indicating
    //that the note was deleted.
    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.title} Deleted", Toast.LENGTH_LONG).show()
    }
}

