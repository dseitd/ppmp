package com.medapp.assistant.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged

@Composable
fun PixelCubesBackground(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.onSizeChanged { }) {
        // SVG: x1="360" y1="446.333" x2="6.68521" y2="-57.1713"
        val start = Offset(x = size.width, y = size.height * 446.333f / 662f)
        val end = Offset(x = size.width * 6.68521f / 360f, y = -size.height * 57.1713f / 662f)
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0F0F0F), // #0F0F0F
                    Color(0xFF293554)  // #293554
                ),
                start = start,
                end = end
            ),
            topLeft = Offset.Zero,
            size = size
        )
    }
} 