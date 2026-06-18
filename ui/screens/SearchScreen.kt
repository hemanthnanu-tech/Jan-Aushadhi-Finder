package com.example.jan_aushadhifinder.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jan_aushadhifinder.data.Medicine
import com.example.jan_aushadhifinder.ui.theme.EmeraldGreen
import com.example.jan_aushadhifinder.ui.theme.HealthcareBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    searchResults: List<Medicine>,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Premium Rounded Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search branded medicine (e.g. Dolo)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HealthcareBlue) },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HealthcareBlue,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (searchResults.isEmpty()) "Quick Compare" else "Results",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (searchResults.isEmpty() && searchQuery.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Search for a medicine to see alternatives", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(searchResults) { medicine ->
                    MedicineCard(medicine)
                }
            }
        }
    }
}

@Composable
fun MedicineCard(medicine: Medicine) {
    var showAIInfo by remember { mutableStateOf(false) }
    var stockStatus by remember { mutableStateOf("Check Availability") }
    var stockColor by remember { mutableStateOf(HealthcareBlue) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // TOP SECTION: Branded
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.brandName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Branded Product | ${medicine.unitSize}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Stock Request (Simulated)
                    TextButton(
                        onClick = {
                            val statuses = listOf("Available", "Low Stock", "Out of Stock")
                            stockStatus = statuses.random()
                            stockColor = when(stockStatus) {
                                "Available" -> EmeraldGreen
                                "Low Stock" -> Color(0xFFFFA000)
                                else -> Color.Red
                            }
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = stockStatus, color = stockColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    text = "₹${medicine.brandedPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    textDecoration = TextDecoration.LineThrough
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // MIDDLE SECTION: Generic Alternative (Blue tinted)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = medicine.genericName,
                            style = MaterialTheme.typography.titleMedium,
                            color = HealthcareBlue,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Jan-Aushadhi Alternative",
                            style = MaterialTheme.typography.labelSmall,
                            color = HealthcareBlue.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = "₹${medicine.genericPrice}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTTOM SECTION: Savings Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = EmeraldGreen.copy(alpha = 0.1f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save ₹${String.format("%.2f", medicine.brandedPrice - medicine.genericPrice)} (${calculateSavingsPercent(medicine)}%)",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                IconButton(onClick = { showAIInfo = !showAIInfo }) {
                    Icon(
                        Icons.Default.Info, 
                        contentDescription = "AI Info", 
                        tint = HealthcareBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (showAIInfo) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = com.example.jan_aushadhifinder.service.GenAIService.getMedicineInsights(medicine.brandName),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

fun calculateSavingsPercent(medicine: Medicine): Int {
    if (medicine.brandedPrice == 0.0) return 0
    return (((medicine.brandedPrice - medicine.genericPrice) / medicine.brandedPrice) * 100).toInt()
}
