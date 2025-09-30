package com.zybooks.dgdyer_lab3_todolist.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// Represents a single to-do item with reactive 'completed' state
data class ToDoItem(
    val id: Int,
    val text: String,
    val completed: MutableState<Boolean> = mutableStateOf(false) // reactive property
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(
    viewModel: ToDoViewModel = viewModel(),
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    // Get the list from the ViewModel (mutableStateListOf ensures Compose recomposes when items change)
    val toDoList = viewModel.toDoList

    // Reactive sorted list so incomplete tasks appear first
    val sortedList by remember(toDoList) {
        derivedStateOf { toDoList.sortedBy { it.completed.value } } // observe 'completed' state
    }

    // Controls whether the dialog is shown
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "To-Do List",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                // Add actions to TopAppBar
                actions = {
                    // Icon button to toggle light/dark theme
                    IconButton(onClick = { onToggleTheme() }) {
                        Icon(
                            // Sun icon if dark mode, moon icon if light mode
                            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BlueTopBar
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(sortedList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Checkbox to mark task as completed (reactive)
                        Checkbox(
                            checked = item.completed.value,
                            onCheckedChange = { checked ->
                                item.completed.value = checked // update reactive state
                            }
                        )

                        // Task text with strike-through if completed
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = if (item.completed.value) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Trash icon button to delete the task
                        IconButton(onClick = { toDoList.remove(item) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Task"
                            )
                        }
                    }
                }
            }
        }
    }

    // Show dialog for adding tasks when showDialog is true
    if (showDialog) {
        AddToDoDialog(
            onDismiss = { showDialog = false },
            onAdd = { text ->
                if (text.isNotBlank()) {
                    toDoList.add(ToDoItem(id = toDoList.size + 1, text = text)) // new tasks start unchecked
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun AddToDoDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    // State for the text input inside the dialog
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New To-Do") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Task") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onAdd(text) // add task
                        text = ""   // reset input
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
