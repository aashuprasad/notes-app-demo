package com.example.notesassignment.database

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
    private lateinit var noteDao: NoteDao
    private lateinit var database: NoteDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetAllNotes() = runBlockingTest {
        val note = Note(
            "Title",
            "Description",
            "Timestamp"
        )
        noteDao.addNote(note)
        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertThat(allNotes.size, `is`(1))
    }

    @Test
    fun updateNote() = runBlockingTest {
        val note = Note(
            "Title",
            "Description",
            "Timestamp"
        )
        noteDao.addNote(note)
        note.title = "New Title"
        noteDao.updateNote(note)
        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertThat(allNotes[0].title, `is`("New Title"))
    }

    @Test
    fun deleteNote() = runBlockingTest {
        val note = Note(
            "Title",
            "Description",
            "Timestamp"
        )
        noteDao.addNote(note)
        val notesBeforeDeletion = noteDao.getAllNotes().getOrAwaitValue()
        Log.d("NoteDaoTest", "Deleting note with title: ${notesBeforeDeletion[0].title}")
        delay(4000)
        noteDao.deleteNote(note)
        delay(4000)
        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        Log.d("NoteDaoTest", "note remaining: ${allNotes[0].title}")
        assertThat(allNotes.size, `is`(0))
    }
}

