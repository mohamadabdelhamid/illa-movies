package com.mabdelhamid.illamovies.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mabdelhamid.illamovies.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * A base class to be inherited by all viewModels is the app.
 *
 * @param STATE the current state of the UI.
 * @param EFFECT the side effects like error messages which we want to show only once.
 * @param EVENT the user actions which need to be processed by the viewModel.
 */

abstract class BaseViewModel<STATE, EFFECT, EVENT> : ViewModel(), CoroutineScope {

    protected val job by lazy { Job() }

    private val initialViewState: STATE by lazy { initViewState() }

    private val _viewState = MutableLiveData(initialViewState)
    val viewState: LiveData<STATE> get() = _viewState

    private val _viewEffect = SingleLiveEvent<EFFECT>()
    val viewEffect: LiveData<EFFECT> get() = _viewEffect

    protected val currentViewState
        get() = requireNotNull(_viewState.value)

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    protected fun updateViewState(state: STATE?) = state?.let { _viewState.value = it }

    protected fun updateViewEffect(effect: EFFECT?) = effect?.let { _viewEffect.value = it }

    protected abstract fun initViewState(): STATE

    abstract fun processEvent(event: EVENT)
}