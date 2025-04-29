package com.medapp.assistant.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medapp.assistant.R
import com.medapp.assistant.data.model.FirstAidGuide
import com.medapp.assistant.navigation.Screen
import com.medapp.assistant.ui.viewmodels.FirstAidGuidesViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed as lazyItemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState as rememberLazyRowState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.itemsIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstAidGuidesScreen(
    navController: NavController
) {
    // Временный статический список руководств
    val allGuides = remember {
        listOf(
            FirstAidGuide(1, "Остановка кровотечения", "Как быстро остановить кровотечение.", "...", null, "Травмы", emptyList(), 0L, false),
            FirstAidGuide(2, "Первая помощь при ожогах", "Что делать при ожогах.", "...", null, "Ожоги", emptyList(), 0L, false),
            FirstAidGuide(3, "Отравления", "Действия при отравлениях.", "...", null, "Отравления", emptyList(), 0L, false),
            FirstAidGuide(4, "Первая помощь при переломах", "Как оказать помощь при переломах.", "...", null, "Травмы", emptyList(), 0L, false),
            FirstAidGuide(5, "Обморок", "Что делать при потере сознания.", "...", null, "Общее", emptyList(), 0L, false)
        )
    }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("Все") + allGuides.map { it.category }.distinct()
    val guideEntities = allGuides
    val coroutineScope = rememberCoroutineScope()
    val lazyRowState = rememberLazyRowState()
    val listState = rememberLazyListState()
    var userSelectedCategory by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
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
    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset, guideEntities) {
        if (!userSelectedCategory && guideEntities.isNotEmpty()) {
            val itemHeightPx = with(density) { 120.dp.toPx() }
            val spacingPx = with(density) { 10.dp.toPx() }
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
            val centerScreen = screenHeightPx / 2f
            val firstVisible = listState.firstVisibleItemIndex
            val scrollOffset = listState.firstVisibleItemScrollOffset.toFloat()
            var closestIdx = firstVisible
            var minDist = Float.MAX_VALUE
            for (i in firstVisible until (firstVisible + 5).coerceAtMost(guideEntities.size)) {
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
            val centerCategory = guideEntities.getOrNull(closestIdx)?.category
            if (centerCategory != null && centerCategory != selectedCategory) {
                selectedCategory = centerCategory
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Первая помощь", style = MaterialTheme.typography.headlineLarge) },
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
            if (guideEntities.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет данных по руководствам",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Без анимации: просто список карточек
                androidx.compose.foundation.lazy.LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(guideEntities) { guide ->
                        GuideCard(guide = guide, onClick = { navController.navigate("first_aid_guide/${guide.id}") })
                    }
                }
            }
        }
    }
}

@Composable
fun FirstAidAnimatedList(
    guides: List<FirstAidGuide>,
    navController: NavController,
    listState: LazyListState = rememberLazyListState()
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
            itemsIndexed(guides) { index, guide ->
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
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                ) {
                    ElevatedCard(
                        onClick = { navController.navigate(Screen.FirstAidGuides.route + "/${guide.id}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .graphicsLayer {
                                this.alpha = alpha
                                this.scaleX = scale
                                this.scaleY = scale
                                this.translationY = translateY
                            },
                        colors = CardDefaults.elevatedCardColors(containerColor = cardColor),
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
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = guide.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = guide.category,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = guide.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GuideCard(
    guide: FirstAidGuide,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = guide.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    AssistChip(
                        onClick = { },
                        label = { Text(guide.category) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocalHospital,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
                IconButton(onClick = onClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Открыть",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = guide.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            if (guide.isOffline) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Доступно офлайн",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
} 