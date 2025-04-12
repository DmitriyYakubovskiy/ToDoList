package com.example.todolist.data_access

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allItems: Flow<List<Todo>> = todoDao.getAll()

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun getById(id: Int): Todo {
        return todoDao.getById(id)
    }

    fun getByIdFlow(id: Int): Flow<Todo> {
        return todoDao.getByIdFlow(id)
    }
}