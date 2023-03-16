package com.example.notesassignment.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {
    private lateinit var database: NoteDatabase
    private lateinit var dao: NoteDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.noteDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun addNote() = runBlocking {
        val note = Note("Test Title", "Test Description", "16 Mar, 2023-18:00")
        dao.addNote(note)

        val allNotes = dao.getAllNotes().getOrAwaitValue()
        assertThat(allNotes).contains(note)
    }
}