package com.example.smarthelmet.ui.screen.Workers



import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthelmet.model.Worker
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkersScreen(
    workersViewModel: WorkersViewModel = viewModel(),
    onWorkerClick: (Worker) -> Unit = {}
) {
    val workers = workersViewModel.workers.collectAsState()

    // Define our custom colors
    val BlueAccent = Color(0xFF47A0F6)
    val LightBlue = Color(0xFFE6F1FE)

    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (showDialog) {
            AddWorkerDialog(
                onDismiss = { showDialog = false },
                onAddWorker = {worker -> workersViewModel.onAddWorker(worker) } // Pass the onAddWorker callback
            )
        }
        TopAppBar(
            title = {
                Text(
                    "Workers",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        Row {

            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                      .padding(16.dp),
                placeholder = { Text("Search workers...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = BlueAccent
                    )
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(12.dp,), // Match the text field's shape
                modifier = Modifier
                    .padding(16.dp), // Add padding around the button
                colors = ButtonDefaults.buttonColors(
                containerColor = BlueAccent, // Background color of the button
                contentColor = Color.White    // Color of the text/icon inside the button
            )

            ) {
                Icon(
                    imageVector = Icons.Filled.Add, // Use a filled plus icon
                    contentDescription = "Add Worker",
                    tint = Color.White // Ensure the icon is visible on the button
                )
            }

        }


        // Workers List
        if (workers.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No workers found",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    workers.value.filter { worker ->
                        searchQuery.isEmpty() || worker.fullName.contains(searchQuery, ignoreCase = true)
                    }
                ) { worker ->
                    WorkerListItem(
                        worker = worker,
                        onClick = { onWorkerClick(worker) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkerListItem(
    worker: Worker,
    onClick: () -> Unit = {}
) {
    Log.d("worker", "WorkerListItem: $worker")

    // Define our custom colors
    val BlueAccent = Color(0xFF47A0F6)
    val DarkText = Color.Black
    val LightBlue = Color(0xFFE6F1FE)

    // Health state color indicator
    val healthStateColor = when(worker.healthState.lowercase()) {
        "healthy", "good" -> Color(0xFF4CAF50)  // Green

        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = BlueAccent.copy(alpha = 0.15f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Worker Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(LightBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Worker",
                    tint = BlueAccent,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Worker Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = worker.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Age: ${worker.age}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Health state indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(healthStateColor, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = worker.healthState,
                        style = MaterialTheme.typography.bodyMedium,
                        color = healthStateColor
                    )
                }
            }

            // Arrow or action indicator
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "View Details",
                tint = BlueAccent.copy(alpha = 0.7f)
            )
        }
    }
}


@Composable
fun AddWorkerDialog(
    onDismiss: () -> Unit,
    onAddWorker: (Worker) -> Unit // Added onAddWorker parameter
) {
    val BlueAccent = Color(0xFF47A0F6)
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var age by remember { mutableStateOf(TextFieldValue("")) }
    var healthState by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface( // Added Surface for background and shape
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(16.dp), // Rounded corners for the dialog
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add Worker")
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = healthState,
                    onValueChange = { healthState = it },
                    label = { Text("Health State") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val worker = Worker(
                                fullName = fullName.text,
                                age = age.text.toIntOrNull() ?: 0, // Handle invalid input
                                healthState = healthState.text
                            )
                            onAddWorker(worker) // Use the callback
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlueAccent, contentColor = Color.White)

                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}