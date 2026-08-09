package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CelestialWhite
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.SereneCyan
import com.example.ui.theme.SereneTeal
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SpiritualGold
import com.example.ui.viewmodel.BreathingPhase

@Composable
fun BreathingShield(
    phase: BreathingPhase,
    remainingSeconds: Int,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val targetScale = when (phase) {
        BreathingPhase.INHALE -> 1.25f
        BreathingPhase.HOLD -> 1.25f
        BreathingPhase.EXHALE -> 0.85f
        BreathingPhase.REST -> 0.85f
    }

    val animatedScale by animateFloatAsState(
        targetValue = if (isPlaying) targetScale else 1.0f,
        animationSpec = tween(
            durationMillis = phase.seconds * 1000,
            easing = FastOutSlowInEasing
        ),
        label = "breathing_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_pulse"
    )

    Box(
        modifier = modifier
            .size(240.dp)
            .testTag("breathing_shield_container"),
        contentAlignment = Alignment.Center
    ) {
        // Outer glowing canvas rings
        Canvas(modifier = Modifier.size(230.dp)) {
            val center = size / 2f
            val radius = (size.minDimension / 2f - 20) * animatedScale

            // Outer Aura Ring
            drawCircle(
                color = SpiritualGold.copy(alpha = pulseAlpha * 0.4f),
                radius = radius * 1.15f,
                style = Stroke(width = 4.dp.toPx())
            )

            // Inner Cyan Protection Shield Ring
            drawCircle(
                color = SereneCyan.copy(alpha = 0.6f),
                radius = radius,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Inner glowing core circle
        Box(
            modifier = Modifier
                .size((140 * animatedScale).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SpiritualGold.copy(alpha = 0.9f),
                            CosmicNavy.copy(alpha = 0.95f),
                            CosmicIndigo
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                val formattedTime = String.format(
                    java.util.Locale.US,
                    "%02d:%02d",
                    remainingSeconds / 60,
                    remainingSeconds % 60
                )

                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CelestialWhite,
                        fontSize = 28.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isPlaying) phase.label else "Ready to Begin",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SoftGold,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}
