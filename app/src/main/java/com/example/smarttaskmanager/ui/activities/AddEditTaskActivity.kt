package com.example.smarttaskmanager.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.database.AppDatabase
import com.example.smarttaskmanager.enum.HeaderTitle
import com.example.smarttaskmanager.enum.TaskPriority
import com.example.smarttaskmanager.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AddEditTaskActivity : BaseActivity() {

    private lateinit var taskTitle: EditText
    private lateinit var taskDescription: EditText
    private lateinit var taskDueDate: TextView
    private lateinit var taskPriority: Spinner
    private lateinit var saveTaskButton: Button
    private lateinit var editTaskButton: Button
    private lateinit var locationTextView: TextView
    private lateinit var taskCompleteSwitch: Switch
    private lateinit var db: AppDatabase // Room database instance
    private var taskId: Long? = null
    private var getTaskResult: Task? = null
    private var headerTitle: String? = null

    companion object {
        private const val REQUEST_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        contentFrameLayout.addView(layoutInflater.inflate(R.layout.activity_add_edit_task, null))
        db = AppDatabase.getDatabase(this)
        taskId = intent.extras?.getString("TASK_ID")?.toLongOrNull()
        headerTitle = intent.extras?.getString("HEADER").toString()
        val tiitle = when (headerTitle) {
            HeaderTitle.EditTask.toString() -> "Edit Task"
            HeaderTitle.AddTask.toString() -> "Add Task"
            HeaderTitle.TaskDetails.toString() -> "Task Details"
            else -> ""
        }
        setCustomHeader(title = tiitle, tittleVisible = true, backButtonVisible = true)
        initViews()
        when (headerTitle) {
            HeaderTitle.EditTask.toString(), HeaderTitle.AddTask.toString() -> {
                saveTaskButton.text = "Save"
                editTaskButton.visibility = View.GONE
            }

            HeaderTitle.TaskDetails.toString() -> {
                editTaskButton.visibility = View.VISIBLE
                saveTaskButton.text = "Delete"
            }
        }

        if (taskId != null) {
            loadTask(taskId!!)
        } else {
            setupNewTask()
        }

        setupDatePicker()
        setupPrioritySpinner()

        locationTextView.setOnClickListener {
            val intent = Intent(this, SelectLocationActivity::class.java)
            startActivityForResult(intent, REQUEST_LOCATION)
        }

        saveTaskButton.setOnClickListener {
            if (saveTaskButton.text.toString() =="Delete") {
                CoroutineScope(Dispatchers.IO).launch {
                    db.taskDao().deleteTaskById(taskId!!)
                    finish()
                }
            } else {
                saveTask()
            }
        }
    }

    private fun initViews() {
        taskTitle = findViewById(R.id.task_title)
        taskDescription = findViewById(R.id.task_description)
        taskDueDate = findViewById(R.id.due_date)
        taskPriority = findViewById(R.id.priority)
        saveTaskButton = findViewById(R.id.btn_delete)
        editTaskButton = findViewById(R.id.btn_edit)
        locationTextView = findViewById(R.id.location)
        taskCompleteSwitch = findViewById(R.id.switch_completion)
    }

    private fun loadTask(taskId: Long) {
        db.taskDao().getTaskById(taskId.toInt()).observe(this, Observer { task ->
            task?.let {
                getTaskResult = it
                updateLayout(it)
                if (headerTitle == HeaderTitle.TaskDetails.toString()) {
                    enableEditing(false)
                    editTaskButton.setOnClickListener {
                        saveTaskButton.text = "Save"
                        enableEditing(true)
                    }
                }
            }
        })
    }

    private fun setupNewTask() {
        editTaskButton.visibility = View.GONE
    }

    private fun enableEditing(enable: Boolean) {
        taskTitle.isEnabled = enable
        taskDescription.isEnabled = enable
        taskDueDate.isEnabled = enable
        locationTextView.isEnabled = enable
        taskPriority.isEnabled = enable
        taskCompleteSwitch.isEnabled = enable
        editTaskButton.isEnabled = !enable
    }

    private fun updateLayout(task: Task) {
        taskTitle.setText(task.title)
        taskDescription.setText(task.description)
        taskDueDate.text = task.dueDate
        val priorities = TaskPriority.values().map { it.name }
        val priorityIndex = TaskPriority.valueOf(task.priority).ordinal
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskPriority.adapter = adapter
        taskPriority.setSelection(priorityIndex)
        locationTextView.text = task.location
        taskCompleteSwitch.isChecked = task.isCompleted
    }

    private fun setupDatePicker() {
        taskDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this, { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    taskDueDate.text = date
                }, year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun setupPrioritySpinner() {
        val priorities = TaskPriority.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskPriority.adapter = adapter

        taskPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                (view as? TextView)?.let { textView ->
                    when (position) {
                        TaskPriority.HIGH.ordinal -> textView.setTextColor(
                            ContextCompat.getColor(
                                this@AddEditTaskActivity, R.color.priority_high
                            )
                        )

                        TaskPriority.MEDIUM.ordinal -> textView.setTextColor(
                            ContextCompat.getColor(
                                this@AddEditTaskActivity, R.color.priority_medium
                            )
                        )

                        TaskPriority.LOW.ordinal -> textView.setTextColor(
                            ContextCompat.getColor(
                                this@AddEditTaskActivity, R.color.priority_low
                            )
                        )

                        TaskPriority.SELECT.ordinal -> textView.setTextColor(
                            ContextCompat.getColor(
                                this@AddEditTaskActivity, android.R.color.darker_gray
                            )
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }

    private fun saveTask() {
        val title = taskTitle.text.toString()
        val description = taskDescription.text.toString()
        val dueDate = formatDate(taskDueDate.text.toString())
        val priority = taskPriority.selectedItem.toString()
        val location = locationTextView.text.toString()
        val isComplete = taskCompleteSwitch.isChecked

        if (title.isNotEmpty() && description.isNotEmpty()) {
            val taskId = this.taskId
            val isNewTask = taskId == null
            val task = getTaskResult?.copy(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                location = location,
                isCompleted = isComplete
            ) ?: Task(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                location = location,
                isCompleted = isComplete
            )

            CoroutineScope(Dispatchers.IO).launch {
                if (isNewTask) {
                    db.taskDao().insertTask(task)
                } else {
                    db.taskDao().updateTask(task)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddEditTaskActivity, "Task saved!", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
            val locationName = data?.getStringExtra("location_name")
            val locationArea = data?.getStringExtra("location_area")
            locationTextView.text = buildString {
                append("Location Name: ")
                append(locationName)
                append(" Location Area: ")
                append(locationArea)
            }
        }
    }

    private fun formatDate(dateString: String): String {
        val parts = dateString.split("/")
        if (parts.size == 3) {
            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2]
            return "$year-${month + 1}-$day"
        }
        return dateString
    }
}
