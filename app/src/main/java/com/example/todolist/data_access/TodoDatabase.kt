package com.example.todolist.data_access

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}