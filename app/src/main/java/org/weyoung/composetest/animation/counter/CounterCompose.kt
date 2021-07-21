package org.weyoung.composetest.animation.counter

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlin.math.pow

@ExperimentalAnimationApi
@Composable
fun Counter(
    onClick: () -> Unit,
    count: Int,
    width: Int
) {
    val displayWidth = maxOf(3, width)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    )
    {
        Row(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        ) {
            for (n in displayWidth - 1 downTo 0) { //<--- this loop builds the row based on the number
                CounterCell(
                    count = (count.toDouble() / 10.0.pow(n) % 10).toInt(), //<--- split the number in to units, tens, hundreds etc
                    width = 1 //<--- set each cell to only be one digit wide
                )
            }
        }
    }
}

const val DURATION = 3500

@ExperimentalAnimationApi
@Composable
fun CounterCell(
    count: Int,
    width: Int
) {
    val numbersSlidingAnimation: AnimatedContentScope<Int>.() -> ContentTransform = {
        if (initialState > targetState) {
            slideInVertically(initialOffsetY = { it }, animationSpec = tween(DURATION)) + fadeIn() with slideOutVertically(
                targetOffsetY = { -it }, animationSpec = tween(DURATION)) + fadeOut()
        } else {
            slideInVertically(initialOffsetY = { -it }, animationSpec = tween(DURATION)) + fadeIn() with slideOutVertically(
                targetOffsetY = { it }, animationSpec = tween(DURATION)) + fadeOut()
        }
    }
    AnimatedContent(
        targetState = count,
        transitionSpec = numbersSlidingAnimation
    ) { number ->
        Text(text = number.toString().padStart(width, '0'), fontSize = 30.sp)
    }
}