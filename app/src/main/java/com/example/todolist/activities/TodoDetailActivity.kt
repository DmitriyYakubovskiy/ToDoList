package com.example.todolist.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var createdAtText: EditText
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

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        completedCheckBox = findViewById(R.id.completedCheckBox)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        val todoId = intent.getIntExtra("TODO_ID", -1)

        if (todoId != -1) {
            deleteButton.visibility = View.VISIBLE

            lifecycleScope.launchWhenCreated {
                viewModel.getTodoById(todoId).collectLatest { todo ->
                    currentTodo = todo
                    titleEditText.setText(todo.name)
                    descriptionEditText.setText(todo.description)
                    completedCheckBox.isChecked = todo.isCompleted
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
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val isCompleted = completedCheckBox.isChecked

        if (title.isBlank()) {
            titleEditText.error = "Title cannot be empty"
            return
        }

        val todo = currentTodo?.copy(
            name = title,
            description = description,
            isCompleted = isCompleted
        ) ?: Todo(
            name = title,
            description = description,
            isCompleted = isCompleted,
            priority = 0,
            deadLineDate = Date(0),
            id=-1
        )

        if (currentTodo == null) {
            viewModel.insert(todo)
        } else {
            viewModel.update(todo)
        }
        finish()
    }
}