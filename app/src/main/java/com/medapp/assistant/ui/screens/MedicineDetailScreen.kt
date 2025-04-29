package com.medapp.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.medapp.assistant.data.model.MedicineEntity
import androidx.hilt.navigation.compose.hiltViewModel
import com.medapp.assistant.ui.viewmodels.MedicineDetailViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import com.medapp.assistant.data.model.MedicineData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    medicineName: String,
    navController: NavController
) {
    val medicineIndex = MedicineData.medicines.indexOfFirst { it.name == medicineName }
    if (medicineIndex == -1) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Медикамент не найден", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }
    var quantity by remember { mutableStateOf(MedicineData.medicines[medicineIndex].quantity) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(MedicineData.medicines[medicineIndex].name, style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Категория: ${MedicineData.medicines[medicineIndex].category}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Формы выпуска:", style = MaterialTheme.typography.titleMedium)
                MedicineData.medicines[medicineIndex].forms.forEach { form ->
                    Text(form, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("Показания: ${MedicineData.medicines[medicineIndex].usage}", style = MaterialTheme.typography.bodyLarge)
            Text("Дозировка: ${MedicineData.medicines[medicineIndex].dosage}", style = MaterialTheme.typography.bodyLarge)
            Text("Срок годности: ${MedicineData.medicines[medicineIndex].expiry}", style = MaterialTheme.typography.bodyLarge)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Количество:", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { if (quantity > 0) quantity-- }) {
                    Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                }
                Text(quantity.toString(), style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = { quantity++ }) {
                    Icon(Icons.Default.Add, contentDescription = "Увеличить")
                }
            }
            Button(
                onClick = {
                    // Для демо: обновляем quantity через копию списка
                    val updated = MedicineData.medicines.toMutableList()
                    updated[medicineIndex] = updated[medicineIndex].copy(quantity = quantity)
                    MedicineData.medicines = updated
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сохранить количество")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = if (highlight) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
} 