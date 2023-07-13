package com.mabdelhamid.illamovies.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.common.UiAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A base class to be inherited by all viewModels is the app.
 *
 * @param STATE the current state of the UI.
 * @param EFFECT the side effects like error messages which we want to show only once.
 * @param EVENT the user actions which need to be processed by the viewModel.
 */

abstract class BaseViewModel<EVENT : ViewEvent, STATE : ViewState, EFFECT : ViewEffect> :
    ViewModel(), CoroutineScope {

    protected val job by lazy { Job() }

    private val initialState: STATE by lazy { initState() }
    protected abstract fun initState(): STATE

    val state: STATE
        get() = viewState.value

    private val _viewEvent: MutableSharedFlow<EVENT> = MutableSharedFlow()
    private val viewEvent = _viewEvent.asSharedFlow()

    private val _viewState: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val viewState = _viewState.asStateFlow()

    private val _viewEffect: Channel<EFFECT> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    private val _viewAlert: Channel<UiAlert> = Channel()
    val viewAlert = _viewAlert.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private fun subscribeToEvents() {
        viewModelScope.launch {
            viewEvent.collect {
                handleEvent(it)
            }
        }
    }

    fun setEvent(event: EVENT) {
        viewModelScope.launch { _viewEvent.emit(event) }
    }

    protected fun setState(reduce: STATE.() -> STATE) {
        _viewState.value = state.reduce()
    }

    protected fun setEffect(builder: () -> EFFECT) {
        viewModelScope.launch { _viewEffect.send(builder()) }
    }

    protected fun setAlert(builder: () -> UiAlert) {
        viewModelScope.launch { _viewAlert.send(builder()) }
    }

    protected abstract fun handleEvent(event: EVENT)
}