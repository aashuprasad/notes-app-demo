package com.example.notesassignment.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class Note(
    val title: String,
    val description: String,
    val timestamp: String,
    ) {
        @PrimaryKey(autoGenerate = true) var id = 0
}