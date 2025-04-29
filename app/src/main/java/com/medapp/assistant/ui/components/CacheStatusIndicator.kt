package com.medapp.assistant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CacheStatusIndicator(
    isFromCache: Boolean,
    lastUpdateTime: Long,
    isCacheValid: Boolean,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val lastUpdate = dateFormat.format(Date(lastUpdateTime))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = when {
                    !isFromCache -> MaterialTheme.colorScheme.primary
                    isCacheValid -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.error
                },
                modifier = Modifier.size(8.dp)
            ) {}

            Text(
                text = when {
                    !isFromCache -> "Свежие данные"
                    isCacheValid -> "Кэш актуален"
                    else -> "Кэш устарел"
                },
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    !isFromCache -> MaterialTheme.colorScheme.primary
                    isCacheValid -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.error
                }
            )
        }

        Text(
            text = "Обновлено: $lastUpdate",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 