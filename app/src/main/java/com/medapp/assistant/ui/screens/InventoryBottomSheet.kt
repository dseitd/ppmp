package com.medapp.assistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.unit.Dp
import com.medapp.assistant.data.model.MedicineData
import com.medapp.assistant.data.model.Medicine
import com.medapp.assistant.data.model.InventoryMedicine
import androidx.hilt.navigation.compose.hiltViewModel
import com.medapp.assistant.data.local.entities.InventoryItem
import com.medapp.assistant.ui.viewmodels.HomeViewModel
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryBottomSheet(onDismiss: () -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = С собой, 1 = Имеется
    val inventory by viewModel.inventory.collectAsState()
    LaunchedEffect(inventory) {
        Log.d("Inventory", "Текущее состояние: $inventory")
    }
    val withMe = inventory.filter { !it.atHome }
    val atHome = inventory.filter { it.atHome }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Инвентарь", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Delete, contentDescription = "Закрыть", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(Modifier.weight(1f)) { TabButton(text = "С собой", selected = selectedTab == 0) { selectedTab = 0 } }
                Box(Modifier.weight(1f)) { TabButton(text = "Имеется", selected = selectedTab == 1) { selectedTab = 1 } }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val list = if (selectedTab == 0) withMe else atHome
            if (list.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Нет медикаментов", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                list.forEach { item ->
                    var swiped by remember { mutableStateOf(false) }
                    var offsetX by remember { mutableStateOf(0.dp) }
                    val animatedOffsetX by animateDpAsState(targetValue = if (swiped) (-72).dp else 0.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .offset(x = animatedOffsetX)
                            .pointerInput(item) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        if (offsetX < (-40).dp) swiped = true
                                        else swiped = false
                                        offsetX = 0.dp
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        offsetX += dragAmount.dp
                                        if (offsetX < (-40).dp) swiped = true
                                        if (offsetX > 0.dp) swiped = false
                                    }
                                )
                            }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Medication,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Text("Срок годности: ${item.expiry ?: "-"}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Количество: ${item.quantity}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        // Мгновенно обновляем UI и сохраняем в Room
                                        val updated = item.copy(atHome = !item.atHome)
                                        viewModel.addInventoryItem(updated)
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (selectedTab == 0) Icons.Default.ArrowForward else Icons.Default.ArrowBack,
                                        contentDescription = if (selectedTab == 0) "В Имеется" else "В С собой",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        if (swiped) {
                            IconButton(
                                onClick = {
                                    viewModel.deleteInventoryItem(item)
                                    swiped = false
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 12.dp)
                                    .background(MaterialTheme.colorScheme.error, CircleShape)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Для хранения статуса "atHome" можно расширить Medicine через data class InventoryMedicine, если потребуется. 