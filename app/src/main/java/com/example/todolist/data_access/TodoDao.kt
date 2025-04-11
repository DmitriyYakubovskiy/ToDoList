package com.example.todolist.data_access

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TodoDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getById(id: Int): Flow<Todo>

    @Query("SELECT * FROM todo WHERE isCompleted = 0 AND deadLineDate < :currentDate ORDER BY deadLineDate ASC")
    fun getOverdueTodos(currentDate: Date): Flow<List<Todo>>
}