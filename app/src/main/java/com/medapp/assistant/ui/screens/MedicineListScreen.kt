package com.medapp.assistant.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medapp.assistant.R
import com.medapp.assistant.data.local.entities.InventoryItemEntity
import com.medapp.assistant.data.local.entities.MedicineEntity
import com.medapp.assistant.data.model.MedicineCategory
import com.medapp.assistant.data.model.MedicineData
import com.medapp.assistant.navigation.Screen
import com.medapp.assistant.ui.viewmodels.MedicineViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private fun interpolate(
    value: Float,
    inputRange: Pair<Float, Float>,
    outputRange: Pair<Float, Float>
): Float {
    val (inputMin, inputMax) = inputRange
    val (outputMin, outputMax) = outputRange
    return outputMin + (outputMax - outputMin) * ((value - inputMin) / (inputMax - inputMin))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val allMedicines = MedicineData.medicines
    var selectedCategory by remember { mutableStateOf("Все") }
    val categories = listOf("Все") + MedicineData.categories.map { it.name }
    val medicineEntities = allMedicines.map {
        MedicineEntity(
            id = 0L,
            name = it.name,
            category = it.category,
            forms = it.forms,
            usage = it.usage,
            dosage = it.dosage,
            expiry = it.expiry,
            isPersonal = false,
            quantity = it.quantity
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val lazyRowState = rememberLazyListState()
    val listState = rememberLazyListState()
    var userSelectedCategory by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var selectedMedicine by remember { mutableStateOf<MedicineEntity?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        lazyRowState.animateScrollToItem(0)
    }

    LaunchedEffect(selectedCategory) {
        val idx = categories.indexOf(selectedCategory)
        if (idx > 0 && userSelectedCategory) {
            coroutineScope.launch {
                lazyRowState.animateScrollToItem(idx)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Медикаменты") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                state = lazyRowState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = {
                            userSelectedCategory = true
                            selectedCategory = category
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            MedicineAnimatedList(
                medicines = medicineEntities.filter { medicine ->
                    selectedCategory == "Все" || selectedCategory == medicine.category
                },
                navController = navController,
                listState = listState,
                onMedicineClick = { selectedMedicine = it }
            )
        }

        if (selectedMedicine != null) {
            var addedCount by remember { mutableStateOf<Int?>(null) }
            ModalBottomSheet(
                onDismissRequest = { selectedMedicine = null },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(selectedMedicine!!.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Формы: ${selectedMedicine!!.forms.joinToString()}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Дозировка: ${selectedMedicine!!.dosage}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Срок годности: ${selectedMedicine!!.expiry}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                    if (addedCount == null) {
                        Button(
                            onClick = {
                                addedCount = 1
                                val med = MedicineData.medicines.find { it.name == selectedMedicine!!.name }
                                if (med != null && MedicineData.trackedMedicines.none { it.medicine.name == med.name }) {
                                    MedicineData.trackedMedicines.add(com.medapp.assistant.data.model.InventoryMedicine(med, atHome = false))
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text("Добавить", style = MaterialTheme.typography.titleLarge)
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { if (addedCount!! > 1) addedCount = addedCount!! - 1 }) {
                                Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                            }
                            Text(
                                text = addedCount.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            IconButton(onClick = { addedCount = addedCount!! + 1 }) {
                                Icon(Icons.Default.Add, contentDescription = "Увеличить")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(onClick = { selectedMedicine = null }) {
                        Text("Закрыть")
                    }
                }
            }
        }
    }
}

@Composable
fun MedicineAnimatedList(
    medicines: List<MedicineEntity>,
    navController: NavController,
    listState: LazyListState = rememberLazyListState(),
    onMedicineClick: (MedicineEntity) -> Unit
) {
    val spacing = 10.dp
    val itemHeight = 120.dp
    val cardColor = MaterialTheme.colorScheme.primaryContainer
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val spacingPx = with(density) { spacing.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = spacing, horizontal = spacing),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            itemsIndexed(medicines) { index, medicine ->
                val itemHeightPx = with(density) { itemHeight.toPx() }
                val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
                val centerScreen = screenHeightPx / 2f
                val firstVisible = listState.firstVisibleItemIndex
                val scrollOffset = listState.firstVisibleItemScrollOffset.toFloat()
                val idxF = index.toFloat()
                val fvF = firstVisible.toFloat()
                val itemTop = (idxF - fvF) * itemHeightPx - scrollOffset + spacingPx * (idxF - fvF)
                val itemCenter = itemTop + itemHeightPx / 2f
                val distanceFromCenter = abs(centerScreen - itemCenter)
                val norm = min(1f, distanceFromCenter / (screenHeightPx / 2f))
                val scale = max(0.92f, 1f - norm * 0.12f)
                val alpha = max(0.5f, 1f - norm * 0.7f)
                val translateY = interpolate(
                    value = norm,
                    inputRange = Pair(0f, 1f),
                    outputRange = Pair(0f, 30f)
                )

                MedicineCardAnimated(
                    medicine = medicine,
                    color = cardColor,
                    height = itemHeight,
                    onClick = { onMedicineClick(medicine) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = itemHeight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .graphicsLayer {
                            this.alpha = alpha
                            this.scaleX = scale
                            this.scaleY = scale
                            this.translationY = translateY
                        }
                )
            }
        }
    }
}

@Composable
fun MedicineCardAnimated(
    medicine: MedicineEntity,
    color: Color,
    height: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
    ) {
        Card(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = color),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Medication,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = medicine.forms.joinToString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Срок годности: ${medicine.expiry}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 