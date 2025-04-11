package com.example.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data_access.Todo
import com.example.todolist.data_access.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val allItems = repository.allItems

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun  update(todo: Todo) = viewModelScope.launch {
        repository.update(todo)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun getTodoById(id:Int) = repository.getTodoById(id)
}