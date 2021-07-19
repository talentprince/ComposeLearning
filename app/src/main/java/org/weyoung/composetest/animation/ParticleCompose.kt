package org.weyoung.composetest.animation

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.sp

const val START_OF_ANIMATION: Int = 0
const val PARTICLE_ANIMATION_DURATION: Int = 2000

@Composable
fun Particle() {
    val transitionState = remember {
        MutableTransitionState(0.1f)
    }
    val transition = updateTransition(transitionState, label = "particle transition")

    val alpha by transition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = PARTICLE_ANIMATION_DURATION
                0f at START_OF_ANIMATION
                1f at (PARTICLE_ANIMATION_DURATION * 0.1f).toInt()
                1f at (PARTICLE_ANIMATION_DURATION * 0.8f).toInt()
                0f at PARTICLE_ANIMATION_DURATION
            }
        },
        label = "alpha animation of particle"
    ) { it }

    val scale by transition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = PARTICLE_ANIMATION_DURATION
                1f at START_OF_ANIMATION
                1f at (PARTICLE_ANIMATION_DURATION * 0.7f).toInt()
                1.5f at PARTICLE_ANIMATION_DURATION
            }
        },
        label = "scale animation of particle"
    ) { it }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Button(onClick = {
                    if (transitionState.currentState == 0.1f) {
                        transitionState.targetState = 0f
                    } else if (transitionState.currentState == 0f) {
                        transitionState.targetState = 0.1f
                    }
                }) { Text("Toggle") }
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .scale(scale)
                        .alpha(alpha),
                    text = "\uD83D\uDD25",
                    fontSize = 50.sp
                )
            }
        }
    }
}