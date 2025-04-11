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

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private val viewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        val adapter = TodoAdapter(
            onItemClicked = { todo ->
                val intent = Intent(this, TodoDetailActivity::class.java).apply {
                    putExtra("TODO_ID", todo.id)
                }
                startActivity(intent)
            },
            onCheckboxClicked = { todo, isChecked ->
                viewModel.update(todo.copy(isCompleted = isChecked))
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.allItems.collectLatest { todos ->
                adapter.submitList(todos)
            }
        }

        fab.setOnClickListener {
            val intent = Intent(this, TodoDetailActivity::class.java)
            startActivity(intent)
        }
    }
}