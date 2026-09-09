package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.IosBorderGlass
import com.example.ui.theme.IosCardGlass
import com.example.ui.theme.IslamicBorderGold

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = IosCardGlass,
    borderColor: Color = IosBorderGlass,
    borderWidth: Dp = 1.dp,
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    if (onClick != null) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val animatedScale by animateFloatAsState(
            targetValue = if (isPressed) 0.98f else 1f,
            animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
            label = "ios_card_press"
        )

        Surface(
            modifier = modifier
                .scale(animatedScale)
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(
                borderWidth,
                Brush.verticalGradient(
                    listOf(
                        borderColor.copy(alpha = 0.35f),
                        borderColor.copy(alpha = 0.08f),
                        IslamicBorderGold.copy(alpha = 0.25f)
                    )
                )
            ),
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x18FFFFFF),
                                Color(0x04FFFFFF),
                                Color(0x00000000)
                            )
                        )
                    )
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    } else {
        Surface(
            modifier = modifier.clip(shape),
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(
                borderWidth,
                Brush.verticalGradient(
                    listOf(
                        borderColor.copy(alpha = 0.35f),
                        borderColor.copy(alpha = 0.08f),
                        IslamicBorderGold.copy(alpha = 0.25f)
                    )
                )
            ),
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x18FFFFFF),
                                Color(0x04FFFFFF),
                                Color(0x00000000)
                            )
                        )
                    )
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}
