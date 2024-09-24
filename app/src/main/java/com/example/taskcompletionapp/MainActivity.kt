package com.example.taskcompletionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskcompletionapp.ui.theme.TaskCompletionAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskCompletionAppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                ) { innerPadding ->
                    Task_Completion(
                        modifier = Modifier
                            .background(Color(0xffb0bec5))
                            .padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun Task_Completion(modifier: Modifier = Modifier) {
    // Task description and list state
    val taskDescription = remember { mutableStateOf("") }
    val taskList = remember { mutableStateOf(mutableListOf<Task>()) }

    // UI layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // OutlinedTextField for task input
        OutlinedTextField(
            value = taskDescription.value,
            onValueChange = { taskDescription.value = it },
            label = { Text("Enter task") },
            colors = OutlinedTextFieldDefaults.colors (
                // Text Colors
                focusedTextColor = Color(0xFF495D92),  // Deep blue for focused text
                unfocusedTextColor = Color(0xFF495D92),  // Deep blue for unfocused text
                // the cursor
                cursorColor = Color(0xFF495D92),  // Cursor matching the deep blue
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        // Button to add task
        Button(
            onClick = {
                if (taskDescription.value.isNotEmpty()) {
                    taskList.value = taskList.value.toMutableList().also {
                        it.add(Task(taskDescription.value, false))
                    }
                    taskDescription.value = ""  // Clear the text field
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        // Display task list with checkboxes
        taskList.value.forEachIndexed { index, task ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = task.description,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    // if the task is completed we strikethrough, otherwise nothing
                    style = if(task.isComplete) {
                        TextStyle(textDecoration = TextDecoration.LineThrough)
                    } else {
                        TextStyle()
                    },
                    color = if(task.isComplete) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color(0xffeeeeeb)
                    }
                )
                Checkbox(
                    checked = task.isComplete,
                    onCheckedChange = { isChecked ->
                        taskList.value = taskList.value.toMutableList().also {
                            it[index] = task.copy(isComplete = isChecked)
                        }
                    }
                )
            }
        }

        // Button to clear completed tasks
        Button(
            onClick = {
                taskList.value = taskList.value.filter { !it.isComplete }.toMutableList()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Completed Tasks")
        }
    }
}


// Data class to hold task information
data class Task(val description: String, val isComplete: Boolean)


@Preview(showBackground = true)
@Composable
fun Task_Completion_Preview() {
    TaskCompletionAppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
        ) { innerPadding ->
            Task_Completion(
                modifier = Modifier
                    .background(Color(0xffb0bec5))
                    .padding(innerPadding),
            )
        }
    }
}