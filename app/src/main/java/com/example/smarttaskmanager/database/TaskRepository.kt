package com.example.smarttaskmanager.database

import androidx.lifecycle.LiveData
import com.example.smarttaskmanager.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(taskId: Long) = withContext(Dispatchers.IO) {
        taskDao.deleteTaskById(taskId)
    }
//    fun searchTasks(query: String): Flow<List<Task>> {
//        return taskDao.searchTasks("%$query%") // Adjust as per your database query method
//    }
}
