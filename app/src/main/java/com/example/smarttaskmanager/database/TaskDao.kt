package com.example.smarttaskmanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smarttaskmanager.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_Entity")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_Entity WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM task_Entity WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long): Int

//    @Query("SELECT * FROM task_Entity WHERE title LIKE :query ORDER BY id ASC")
//    fun searchTasks(query: String): Flow<List<Task>>
}
