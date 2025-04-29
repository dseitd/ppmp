package com.medapp.assistant.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medapp.assistant.R
import com.medapp.assistant.data.model.MedicineEntity
import com.medapp.assistant.navigation.Screen
import com.medapp.assistant.ui.viewmodels.MedicineListViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import com.medapp.assistant.data.model.MedicineData
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.combinedClickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.material.icons.filled.Medication
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed as lazyItemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState as rememberLazyRowState
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    // viewModel: MedicineListViewModel = hiltViewModel() // Отключаем старый viewModel, работаем с MedicineData
) {
    val allMedicines = MedicineData.medicines
    var selectedCategory by remember { mutableStateOf<String?>(null) }
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
    val lazyRowState = rememberLazyRowState()
    val listState = rememberLazyListState()
    var userSelectedCategory by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    // BottomSheet state
    var selectedMedicine by remember { mutableStateOf<MedicineEntity?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    // Центрируем выбранную категорию
    LaunchedEffect(selectedCategory) {
        val idx = categories.indexOf(selectedCategory ?: "Все")
        if (idx >= 0) {
            coroutineScope.launch {
                lazyRowState.animateScrollToItem(idx.coerceAtLeast(0))
            }
        }
    }
    // Синхронизация категории с центральной карточкой при скролле
    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset, medicineEntities) {
        if (!userSelectedCategory && medicineEntities.isNotEmpty()) {
            val itemHeightPx = with(density) { 120.dp.toPx() }
            val spacingPx = with(density) { 10.dp.toPx() }
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
            val centerScreen = screenHeightPx / 2f
            val firstVisible = listState.firstVisibleItemIndex
            val scrollOffset = listState.firstVisibleItemScrollOffset.toFloat()
            var closestIdx = firstVisible
            var minDist = Float.MAX_VALUE
            for (i in firstVisible until (firstVisible + 5).coerceAtMost(medicineEntities.size)) {
                val idxF = i.toFloat()
                val fvF = firstVisible.toFloat()
                val itemTop = (idxF - fvF) * itemHeightPx - scrollOffset + spacingPx * (idxF - fvF)
                val itemCenter = itemTop + itemHeightPx / 2f
                val dist = abs(centerScreen - itemCenter)
                if (dist < minDist) {
                    minDist = dist
                    closestIdx = i
                }
            }
            val centerCategory = medicineEntities.getOrNull(closestIdx)?.category
            if (centerCategory != null && centerCategory != selectedCategory) {
                selectedCategory = centerCategory
            }
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Медикаменты", style = MaterialTheme.typography.headlineLarge) },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Категории — горизонтальный LazyRow с автоцентрированием и подсветкой
            LazyRow(
                state = lazyRowState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                lazyItemsIndexed(categories) { idx, category ->
                    val isSelected = selectedCategory == category || (selectedCategory == null && category == "Все")
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory = if (category == "Все") null else category
                            userSelectedCategory = true
                            coroutineScope.launch {
                                // Через 500мс сбрасываем userSelectedCategory, чтобы снова синхронизировать с прокруткой
                                kotlinx.coroutines.delay(500)
                                userSelectedCategory = false
                            }
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            if (medicineEntities.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет данных по медикаментам",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                MedicineAnimatedList(
                    medicines = medicineEntities,
                    navController = navController,
                    listState = listState,
                    onMedicineClick = { selectedMedicine = it }
                )
            }
        }
        // BottomSheet
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
                                // Добавляем в отслеживаемые, если ещё не добавлен
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
                    norm,
                    inputRange = 0f to 1f,
                    outputRange = 0f to 30f
                )
                MedicineCardAnimated(
                    medicine = medicine,
                    color = cardColor,
                    height = itemHeight,
                    onClick = { onMedicineClick(medicine) },
                    modifier = Modifier
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

fun interpolate(value: Float, inputRange: Pair<Float, Float>, outputRange: Pair<Float, Float>): Float {
    val (inMin, inMax) = inputRange
    val (outMin, outMax) = outputRange
    val t = ((value - inMin) / (inMax - inMin)).coerceIn(0f, 1f)
    return outMin + (outMax - outMin) * t
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

@Composable
fun ModernMedicineCard(
    medicine: MedicineEntity,
    onClick: () -> Unit,
    onAddToPersonal: () -> Unit
) {
    val isExpiringSoon = remember(medicine.expiry) {
        // Пример: если срок годности содержит "2024" — выделить
        medicine.expiry.contains("2024")
    }
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpiringSoon) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp)
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
                tint = if (isExpiringSoon) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
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
                    color = if (isExpiringSoon) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onAddToPersonal) {
                Icon(
                    imageVector = if (medicine.isPersonal) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (medicine.isPersonal) "Убрать из личных" else "В личные",
                    tint = if (medicine.isPersonal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 