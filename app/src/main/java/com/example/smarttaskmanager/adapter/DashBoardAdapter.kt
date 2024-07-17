package com.example.smarttaskmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

class DashBoardAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<DashBoardAdapter.TaskViewHolder>() {

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_dash_borad, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTaskTitle.text = task.title
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.tvTaskDueDate.text = sdf.format(task.dueDate)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDueDate: TextView = itemView.findViewById(R.id.tvTaskDueDate)
    }
}
