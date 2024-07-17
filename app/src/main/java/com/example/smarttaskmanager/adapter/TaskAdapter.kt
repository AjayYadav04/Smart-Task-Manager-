package com.example.smarttaskmanager.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.enum.TaskPriority
import com.example.smarttaskmanager.model.Task
import com.example.smarttaskmanager.utils.AppUtils

class TaskAdapter(
    private var taskList: List<Task>,
    private val listener: OnTaskItemClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnTaskItemClickListener {
        fun onTaskItemClick(task: Task)
        fun onTaskItemEdit(task: Task)
        fun onTaskItemDelete(task: Long)
        fun onTaskCompletedChanged(task: Task, isCompleted: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_adapter, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount() = taskList.size

    fun setTasks(tasks: List<Task>) {
        taskList = tasks
        notifyDataSetChanged()
    }


    fun enableSwipeGesture(recyclerView: RecyclerView, context: Context) {
        val deleteBackground = ColorDrawable(Color.RED)
        val editBackground = ColorDrawable(Color.BLUE)
        val deleteIcon = ContextCompat.getDrawable(context, R.drawable.trash)!!
        val editIcon = ContextCompat.getDrawable(context, R.drawable.edit)!!

        val touchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // We don't support move in this scenario
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = taskList[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()

                        listener.onTaskItemDelete(task.id)
                    }
                    ItemTouchHelper.RIGHT -> {
                        Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
                        listener.onTaskItemEdit(task)
                    }
                }
            }

//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//
//                val itemView = viewHolder.itemView
//                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
//                val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
//                val iconBottom = iconTop + deleteIcon.intrinsicHeight
//
//                when {
//                    dX > 0 -> { // Swiping to the right
//                        editBackground.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
//                        val iconLeft = itemView.left + iconMargin
//                        val iconRight = iconLeft + editIcon.intrinsicWidth
//                        editIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//                        editBackground.draw(c)
//                        editIcon.draw(c)
//                    }
//                    dX < 0 -> { // Swiping to the left
//                        deleteBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
//                        val iconRight = itemView.right - iconMargin
//                        val iconLeft = iconRight - deleteIcon.intrinsicWidth
//                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//                        deleteBackground.draw(c)
//                        deleteIcon.draw(c)
//                    }
//                    else -> { // View is unSwiped
//                        deleteBackground.setBounds(0, 0, 0, 0)
//                        editBackground.setBounds(0, 0, 0, 0)
//                    }
//                }
//            }
        }

        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.textTaskTitle)
        private val taskDueDate: TextView = itemView.findViewById(R.id.textTaskDueDate)
        private val taskPriority: TextView = itemView.findViewById(R.id.textTaskPriority)
        private val taskCompletedCheckbox: CheckBox = itemView.findViewById(R.id.checkboxTaskCompleted)

        fun bind(task: Task) {
            taskTitle.text = task.title
            taskDueDate.text = "Due Date: ${task.dueDate}"
            setPriority(task.priority)
            taskCompletedCheckbox.isChecked = task.isCompleted

            itemView.setOnClickListener { listener.onTaskItemClick(task) }
            itemView.setOnLongClickListener {
                listener.onTaskItemEdit(task)
                true
            }
            taskCompletedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                listener.onTaskCompletedChanged(task, isChecked)
            }
        }

        private fun setPriority(priority: String) {
            val priorityColor = when (priority.toUpperCase()) {
                TaskPriority.HIGH.toString() -> R.color.priority_high
                TaskPriority.MEDIUM.toString() -> R.color.priority_medium
                TaskPriority.LOW.toString() -> R.color.priority_low
                else -> android.R.color.darker_gray
            }
            taskPriority.setTextColor(ContextCompat.getColor(AppUtils.appContext!!, priorityColor))
            taskPriority.text = getPriorityText(priority)
        }

        private fun getPriorityText(priority: String): String {
            return when (priority.toUpperCase()) {
                TaskPriority.HIGH.toString() -> "High Priority"
                TaskPriority.MEDIUM.toString() -> "Medium Priority"
                TaskPriority.LOW.toString() -> "Low Priority"
                else -> ""
            }
        }
    }
}
