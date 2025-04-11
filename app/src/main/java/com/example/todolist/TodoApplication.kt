package com.example.todolist

import android.app.Application
import androidx.room.Room
import com.example.todolist.data_access.TodoDatabase
import com.example.todolist.data_access.TodoRepository

class TodoApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(this, TodoDatabase::class.java, "todo_database").build()
    }
    val repository by lazy { TodoRepository(database.todoDao()) }
}