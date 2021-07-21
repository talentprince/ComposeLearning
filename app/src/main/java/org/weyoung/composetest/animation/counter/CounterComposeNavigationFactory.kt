package org.weyoung.composetest.animation.counter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.lachlanmckee.hilt.compose.navigation.factory.ComposeNavigationFactory
import net.lachlanmckee.hilt.compose.navigation.factory.HiltComposeNavigationFactory
import net.lachlanmckee.hilt.compose.navigation.factory.viewModelComposable
import javax.inject.Inject

@HiltComposeNavigationFactory
class CounterComposeNavigationFactory @Inject constructor() : ComposeNavigationFactory {
    @ExperimentalAnimationApi
    override fun create(builder: NavGraphBuilder, navController: NavHostController) {
        builder.viewModelComposable<CounterViewModel>("feature2") {
            val count by count.collectAsState()
            Counter(onClick = { toggle() }, count = count, width = 8)
        }
    }
}

class CounterViewModel : ViewModel() {
    private val isRuning = false
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    private var state = CounterState.INCREMENT


    fun toggle() {
        state = when (state) {
            CounterState.INCREMENT -> CounterState.DECREMENT
            CounterState.DECREMENT -> CounterState.INCREMENT
        }
        startTimer()
    }

    private fun startTimer() {
        if (!isRuning) {
            viewModelScope.launch {
                while (true) {
                    if (_count.value == 9999999) {
                        state = CounterState.DECREMENT
                    } else if (_count.value == 0) {
                        state = CounterState.INCREMENT
                    }
                    when (state) {
                        CounterState.INCREMENT -> _count.value++
                        CounterState.DECREMENT -> _count.value--
                    }
                    delay(500)
                }
            }
        }    }
    enum class CounterState {
        INCREMENT,
        DECREMENT
    }
}
