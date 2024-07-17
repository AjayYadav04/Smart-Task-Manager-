package com.example.smarttaskmanager.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.adapter.TaskAdapter
import com.example.smarttaskmanager.database.TaskViewModel
import com.example.smarttaskmanager.enum.HeaderTitle
import com.example.smarttaskmanager.model.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp

class HomeActivity : BaseActivity(), TaskAdapter.OnTaskItemClickListener {

    private lateinit var adapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        contentFrameLayout.addView(layoutInflater.inflate(R.layout.home_activity, null))
        setCustomHeader("Task Manager", tittleVisible = true, backButtonVisible = false)
        FirebaseApp.initializeApp(this)

        val navView: BottomNavigationView = findViewById(R.id.navigation)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    recreate()
                    true
                }

                R.id.navigation_dashboard ->{
                    val intent = Intent(this, DashBoardActivity::class.java)
                    intent.putExtra("HEADER", HeaderTitle.Dashboard.toString())
                    startActivity(intent)
                    true
                }
                R.id.navigation_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.putExtra("HEADER", HeaderTitle.Setting.toString())
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.task_list)
        adapter = TaskAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.enableSwipeGesture(recyclerView, this)
        taskViewModel.allTasks.observe(this) { tasks ->
            tasks?.let { adapter.setTasks(it) }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            intent.putExtra("HEADER", HeaderTitle.AddTask.toString())
            startActivity(intent)
        }
//        val searchBar = findViewById<EditText>(R.id.search_bar)
//        searchBar.setOnEditorActionListener { _, _, _ ->
//            searchTasks(searchBar.text.toString())
//            true
//        }
    }

    override fun onTaskItemClick(task: Task) {
        val intent = Intent(this, AddEditTaskActivity::class.java)
        intent.putExtra("TASK_ID", task.id.toString())
        intent.putExtra("HEADER", HeaderTitle.TaskDetails.toString())
        startActivity(intent)
        recreate()
    }

    override fun onTaskItemEdit(task: Task) {
        val intent = Intent(this, AddEditTaskActivity::class.java).apply {
            putExtra("TASK_ID", task.id.toString())
            putExtra("HEADER", HeaderTitle.EditTask.toString())
        }
        startActivity(intent)
        recreate()
    }

    override fun onTaskItemDelete(task: Long) {
        taskViewModel.delete(task)
        recreate()
    }

    override fun onTaskCompletedChanged(task: Task, isCompleted: Boolean) {
        taskViewModel.update(task.copy(isCompleted = isCompleted))
    }

    //    private fun searchTasks(query: String) {
//        taskViewModel.searchTasks(query).observe(this) { tasks ->
//            tasks?.let { adapter.setTasks(it) }
//        }
//    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
