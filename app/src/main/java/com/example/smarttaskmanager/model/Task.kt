package com.example.smarttaskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_Entity")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var title: String,
    var description: String,
    var dueDate: String,
    var priority: String,
    val isCompleted: Boolean = false,
    var location: String? = null
)
