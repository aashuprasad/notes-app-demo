package com.example.notesassignment.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
      //  assertThat(allNotes.size, `is`(1))
        noteDao.deleteNote(note)
        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertThat(allNotes.size, `is`(0))
    }
}

