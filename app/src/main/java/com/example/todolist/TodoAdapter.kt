package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data_access.Todo
import java.text.SimpleDateFormat
import java.util.TimeZone

class TodoAdapter(
    private val onItemClicked: (Todo) -> Unit,
    private val onCheckboxClicked: (Todo, Boolean) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemClicked, onCheckboxClicked)
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val createDate: TextView = itemView.findViewById(R.id.createDateTextView)
        private val completedCheckbox: CheckBox = itemView.findViewById(R.id.completedCheckbox)
        fun bind(
            todo: Todo,
            onItemClicked: (Todo) -> Unit,
            onCheckboxClicked: (Todo, Boolean) -> Unit
        ) {
            titleTextView.text = todo.name
            descriptionTextView.text = todo.description
            completedCheckbox.isChecked = todo.isCompleted
            createDate.text = SimpleDateFormat("MMMM dd, yyyy HH:mm:ss").format(todo.createdAt)

            itemView.setOnClickListener {
                onItemClicked(todo)
            }

            completedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxClicked(todo, isChecked)
            }
        }

        companion object {
            fun create(parent: ViewGroup): TodoViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_todo, parent, false)
                return TodoViewHolder(view)
            }
        }
    }

    class TodoComparator : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
}