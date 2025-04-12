package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.TodoAdapter
import com.example.todolist.TodoApplication
import com.example.todolist.TodoViewModel
import com.example.todolist.TodoViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private val viewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((application as TodoApplication).repository)
    }

    private val adapter = TodoAdapter(
        onItemClicked = { todo ->
            val intent = Intent(this, TodoDetailActivity::class.java).apply {
                putExtra("TODO_ID", todo.id)
            }
            startActivity(intent)
        },
        onCheckboxClicked = { todo, isChecked ->
            lifecycleScope.launch {
                viewModel.getByIdFlow(todo.id)
                    .take(1)
                    .collect { freshTodo ->
                        freshTodo?.let {
                            viewModel.update(it.copy(
                                isCompleted = isChecked,
                                updatedAt = Date()
                            ))
                        }
                    }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.allItems.collectLatest { todos ->
                adapter.submitList(todos)
            }
        }

        fab.setOnClickListener {
            startActivity(Intent(this, TodoDetailActivity::class.java))
        }
    }
}