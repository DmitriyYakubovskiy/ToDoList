package com.example.todolist

import android.util.Log
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

    suspend fun getById(id: Int): Todo {
        return repository.getById(id)
    }

    fun update(todo: Todo) = viewModelScope.launch {
        try {
            repository.update(todo)
        } catch (e: Exception) {
            Log.e("TodoViewModel", "Error updating todo", e)
        }
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun getByIdFlow(id:Int) = repository.getByIdFlow(id)
}