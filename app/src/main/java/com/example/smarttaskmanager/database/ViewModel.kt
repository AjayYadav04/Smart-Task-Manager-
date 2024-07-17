package com.example.smarttaskmanager.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.smarttaskmanager.model.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }

    fun getTaskById(task: Int) = viewModelScope.launch {
        repository.getTaskById(task)
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun delete(task: Long) = viewModelScope.launch {
        repository.deleteTask(task)
    }
//    fun searchTasks(query: String): LiveData<List<Task>> {
//        return repository.searchTasks(query).asLiveData()
//    }
}