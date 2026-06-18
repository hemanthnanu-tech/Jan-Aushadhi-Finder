package com.example.jan_aushadhifinder.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jan_aushadhifinder.data.Reminder
import com.example.jan_aushadhifinder.ui.MainViewModel
import com.example.jan_aushadhifinder.util.ReminderManager
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val reminders by viewModel.reminders.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newMedName by remember { mutableStateOf("") }
    var newDosage by remember { mutableStateOf("") }
    
    val calendar = remember { Calendar.getInstance() }
    var selectedDateTime by remember { mutableLongStateOf(0L) }

    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault()) }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Set Medicine Reminder") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newMedName, 
                        onValueChange = { newMedName = it }, 
                        label = { Text("Medicine Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDosage, 
                        onValueChange = { newDosage = it }, 
                        label = { Text("Dosage (e.g., 1 Tablet)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Date/Time Picker Trigger
                    Box(modifier = Modifier.fillMaxWidth().clickable {
                        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            
                            TimePickerDialog(context, { _, hourOfDay, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)
                                calendar.set(Calendar.SECOND, 0)
                                selectedDateTime = calendar.timeInMillis
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                        }
                        
                        DatePickerDialog(
                            context, 
                            dateSetListener, 
                            calendar.get(Calendar.YEAR), 
                            calendar.get(Calendar.MONTH), 
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        OutlinedTextField(
                            value = if (selectedDateTime == 0L) "" else dateFormatter.format(Date(selectedDateTime)),
                            onValueChange = {},
                            label = { Text("Schedule Time") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newMedName.isNotBlank() && selectedDateTime != 0L) {
                        val reminder = Reminder(
                            medicineName = newMedName,
                            dosage = newDosage,
                            dateTime = selectedDateTime
                        )
                        viewModel.addReminder(reminder)
                        
                        // Schedule Actual Notification
                        ReminderManager.scheduleReminder(context, newMedName, newDosage, calendar)
                        
                        showAddDialog = false
                        newMedName = ""
                        newDosage = ""
                        selectedDateTime = 0L
                    }
                }) { Text("Save Reminder") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }, 
                containerColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Reminder", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "My Reminders",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (reminders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Text("No reminders set.", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(reminders) { reminder ->
                        ReminderCard(reminder) {
                            viewModel.deleteReminder(reminder)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderCard(reminder: Reminder, onDelete: () -> Unit) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault()) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        Icons.Default.Notifications, 
                        contentDescription = null, 
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = reminder.medicineName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = reminder.dosage, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        text = dateFormatter.format(Date(reminder.dateTime)), 
                        style = MaterialTheme.typography.labelSmall, 
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Reminder", tint = Color.LightGray)
            }
        }
    }
}

private fun Modifier.size(size: Dp): Modifier = this.then(Modifier.width(size).height(size))
