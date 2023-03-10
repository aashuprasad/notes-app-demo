package com.example.notesassignment.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class Note(
    @PrimaryKey(autoGenerate = true)val id: Int=0,
    val title:String,
    val content:String,
    val timestamp:Long = System.currentTimeMillis()
)