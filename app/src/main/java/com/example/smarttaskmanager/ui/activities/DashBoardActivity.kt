package com.example.smarttaskmanager.ui.activities

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.adapter.DashBoardAdapter
import com.example.smarttaskmanager.database.TaskViewModel
import com.example.smarttaskmanager.model.Task
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class DashBoardActivity : BaseActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var pieCompletedTasks: PieChart
    private lateinit var chartPendingTasks: PieChart
    private lateinit var rvUpcomingTasks: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        contentFrameLayout.addView(layoutInflater.inflate(R.layout.home_activity, null))
        setCustomHeader("Dash Board", tittleVisible = true, backButtonVisible = false)
//        pieCompletedTasks = findViewById(R.id.chartCompletedTasks)
        chartPendingTasks = findViewById(R.id.chartPendingTasks)
        rvUpcomingTasks = findViewById(R.id.rvUpcomingTasks)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        taskViewModel.allTasks.observe(this) { tasks ->
            tasks?.let {
                updateCharts(it)
                updateUpcomingTasksList(it)
            }
        }

        val tvPerformanceInsights = findViewById<TextView>(R.id.tvPerformanceInsights)
        tvPerformanceInsights.text = "Machine learning-generated tips for improving task management will appear here."
    }

    private fun updateCharts(tasks: List<Task>) {
        val completedTasks = tasks.count { it.isCompleted }
        val pendingTasks = tasks.size - completedTasks

        val completedEntries = listOf(
            PieEntry(completedTasks.toFloat(), "Completed"),
            PieEntry(pendingTasks.toFloat(), "Pending")
        )

        val completedDataSet = PieDataSet(completedEntries, "Task Completion")
//        pieCompletedTasks.data = PieData(completedDataSet)
//        pieCompletedTasks.invalidate()

        val pendingEntries = listOf(
            PieEntry(pendingTasks.toFloat(), "Pending"),
            PieEntry(completedTasks.toFloat(), "Completed")
        )

        val pendingDataSet = PieDataSet(pendingEntries, "Task Completion")
        chartPendingTasks.data = PieData(pendingDataSet)
        chartPendingTasks.invalidate()
    }

    private fun updateUpcomingTasksList(tasks: List<Task>) {
        val adapter = DashBoardAdapter(tasks)
        rvUpcomingTasks.adapter = adapter
    }
}
