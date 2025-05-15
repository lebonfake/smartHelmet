package com.example.smarthelmet.ui.screen.incidents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowDropDown

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smarthelmet.R

// Define our custom colors
private val BlueAccent = Color(0xFF47A0F6)
private val DarkText = Color.Black
private val LightBlue = Color(0xFFE6F1FE)
private val MediumBlue = Color(0xFFADD8FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilter(helmetId: MutableState<String>, sortByDate: MutableState<Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = BlueAccent.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.filtre_icon),
                        contentDescription = "Filter",
                        tint = BlueAccent,
                        modifier = Modifier
                            .size(28.dp)
                            .background(LightBlue, CircleShape)
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Filter Incidents",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                // Reset filters button (optional)
                TextButton(
                    onClick = {
                        helmetId.value = ""
                        sortByDate.value = 0
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = BlueAccent
                    )
                ) {
                    Text("Reset", fontWeight = FontWeight.Medium)
                }
            }

            Divider(
                color = Color.LightGray.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            // Helmet ID search field with enhanced styling
            OutlinedTextField(
                value = helmetId.value,
                onValueChange = {
                    helmetId.value = it
                    Log.d("search", "SearchFilter: ${helmetId.value}")
                },
                label = { Text("Search by Helmet ID", color = DarkText.copy(alpha = 0.7f)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = BlueAccent,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = DarkText,
                    cursorColor = BlueAccent,
                    focusedBorderColor = BlueAccent,
                    unfocusedBorderColor = Color.LightGray,
                    containerColor = Color.White
                )
            )

            // Sort by date with better styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.sort_icon),
                    contentDescription = "Sort",
                    tint = BlueAccent,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                SortByDropdown(sortByDate)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortByDropdown(sortByDate: MutableState<Int>) {
    val options = listOf("Newest First", "Oldest First")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = options[sortByDate.value],
            onValueChange = {},
            readOnly = true,
            label = { Text("Sort by Date", color = DarkText.copy(alpha = 0.7f)) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = BlueAccent
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = DarkText,
                cursorColor = BlueAccent,
                focusedBorderColor = BlueAccent,
                unfocusedBorderColor = Color.LightGray,
                containerColor = if (expanded) LightBlue else Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(60.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .exposedDropdownSize()
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = BlueAccent.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = DarkText,
                            fontWeight = if (sortByDate.value == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        sortByDate.value = index
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = DarkText,
                        leadingIconColor = BlueAccent,
                        trailingIconColor = BlueAccent,
                        disabledLeadingIconColor = Color.Gray,
                        disabledTrailingIconColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    ),
                    modifier = Modifier
                        .background(
                            if (sortByDate.value == index) LightBlue else Color.White
                        )
                )
            }
        }
    }
}