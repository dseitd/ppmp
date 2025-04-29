package com.medapp.assistant.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.medapp.assistant.data.model.MedicineEntity
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListByCategoryScreen(
    navController: NavController,
    category: String,
    medicines: List<MedicineEntity>,
    onMedicineClick: (MedicineEntity) -> Unit
) {
    val filteredMedicines = medicines.filter { it.category == category }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(category) },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Добавляю анимированный прогресс-бар
            AnimatedDonutRow(medicines = medicines, category = category)
            if (filteredMedicines.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет препаратов в этой категории", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                filteredMedicines.forEach { medicine ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMedicineClick(medicine) },
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Medication,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = medicine.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = medicine.forms.joinToString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedDonut(
    modifier: Modifier = Modifier,
    current: Int,
    max: Int,
    color: Color,
    inactiveColor: Color = Color(0xFF353C51),
    size: Dp = 62.dp,
    strokeWidth: Dp = 5.dp,
    duration: Int = 800,
    delay: Int = 0,
    label: String = "",
    subLabel: String = ""
) {
    val animatedProgress = remember { Animatable(0f) }
    val target = current.toFloat() / max.toFloat()
    LaunchedEffect(current, max) {
        delay(delay.toLong())
        animatedProgress.animateTo(target, animationSpec = tween(duration))
    }
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweep = 360f * animatedProgress.value
            val arcRect = Size(size.toPx(), size.toPx())
            // Фон
            drawArc(
                color = inactiveColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // Прогресс
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$current",
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$max",
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun AnimatedDonutRow(
    medicines: List<MedicineEntity>,
    category: String? = null
) {
    val categories = if (category == null || category == "Все") {
        medicines.map { it.category }.distinct()
    } else listOf(category)
    val data = categories.map { cat ->
        val meds = medicines.filter { it.category == cat }
        val current = meds.sumOf { it.quantity }
        val max = meds.size * 30 // 30 — условный максимум на препарат
        Triple(cat, current, max)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        data.forEach { (cat, current, max) ->
            AnimatedDonut(
                current = current,
                max = max.coerceAtLeast(1),
                color = MaterialTheme.colorScheme.primary,
                label = cat
            )
        }
    }
} 