package com.zybooks.dgdyer_lab3_todolist.ui.theme

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

// ViewModel holds your data so it survives orientation changes
class ToDoViewModel : ViewModel() {
    // The list of tasks
    val toDoList = mutableStateListOf<ToDoItem>()
}