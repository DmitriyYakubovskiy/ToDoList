package com.example.todolist.data_access

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "todo")
@TypeConverters(DateConverter::class)
data class Todo (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name:String,
    val description:String,
    val priority: Int = 0,
    val isCompleted:Boolean = false,
    val deadLineDate: Date = Date(System.currentTimeMillis()),
    val createdAt: Date = Date(System.currentTimeMillis()),
    val updatedAt: Date = Date(System.currentTimeMillis())
)