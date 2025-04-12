package com.example.todolist.activities

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.TodoViewModel
import com.example.todolist.TodoViewModelFactory
import com.example.todolist.data_access.Todo
import kotlinx.coroutines.flow.collectLatest
import java.util.Date

class TodoDetailActivity : AppCompatActivity() {
    private lateinit var nameText: EditText
    private lateinit var descriptionText: EditText
    private lateinit var createDateText: TextView
    private lateinit var completedCheckBox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private val viewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((application as TodoApplication).repository)
    }
    private var currentTodo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        nameText = findViewById(R.id.nameEditText)
        descriptionText = findViewById(R.id.descriptionEditText)
        completedCheckBox = findViewById(R.id.completedCheckBox)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        createDateText = findViewById(R.id.createDateTextView)

        val todoId = intent.getIntExtra("TODO_ID", -1)

        if (todoId != -1) {
            deleteButton.isEnabled = true

            lifecycleScope.launchWhenCreated {
                viewModel.getByIdFlow(todoId).collectLatest { todo ->
                    if (todo != null) {
                        currentTodo = todo
                        nameText.setText(todo.name)
                        descriptionText.setText(todo.description)
                        completedCheckBox.isChecked = todo.isCompleted
                        createDateText.text = "Created: ${todo.createdAt}"
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            saveTodo()
        }

        deleteButton.setOnClickListener {
            currentTodo?.let { todo ->
                viewModel.delete(todo)
                finish()
            }
        }
    }

    private fun saveTodo() {
        val name = nameText.text.toString()
        val description = descriptionText.text.toString()
        val isCompleted = completedCheckBox.isChecked

        if (name.isBlank()) {
            nameText.error = "Title cannot be empty"
            return
        }

        val todo = currentTodo?.copy(
            name = name,
            description = description,
            isCompleted = isCompleted,
            updatedAt = Date(System.currentTimeMillis())
        ) ?: Todo(
            name = name,
            description = description,
            isCompleted = isCompleted,
            priority = 0
        )

        if (currentTodo == null) {
            viewModel.insert(todo)
        } else {
            viewModel.update(todo)
        }
        finish()
    }
}