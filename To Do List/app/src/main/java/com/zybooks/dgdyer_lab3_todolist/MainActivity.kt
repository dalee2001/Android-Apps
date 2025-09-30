package com.zybooks.dgdyer_lab3_todolist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.zybooks.dgdyer_lab3_todolist.ui.theme.DgDyer_Lab3_ToDoListTheme
import com.zybooks.dgdyer_lab3_todolist.ui.theme.ToDoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // State to track dark mode
            var darkTheme by remember { mutableStateOf(false) }

            // Wrap your UI in MaterialTheme using the current darkTheme state
            DgDyer_Lab3_ToDoListTheme(
                darkTheme = darkTheme // Pass the current theme state
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass darkTheme and onToggleTheme to ToDoScreen
                    ToDoScreen(
                        darkTheme = darkTheme,
                        onToggleTheme = { darkTheme = !darkTheme } // Lambda to toggle theme
                    )
                }
            }
        }
    }
}


