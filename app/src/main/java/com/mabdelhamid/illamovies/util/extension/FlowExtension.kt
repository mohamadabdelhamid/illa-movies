package com.mabdelhamid.illamovies.util.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Inline function for collecting values from a Flow only when the lifecycle is in the STARTED state.
 *
 * This function collects values emitted by the Flow only when the provided [owner]'s lifecycle is in the STARTED state.
 * It uses the [repeatOnLifecycle] function to automatically restart the collection when the lifecycle reaches the STARTED state again.
 * The collected values are passed to the [onCollect] suspending lambda for further processing.
 *
 * @param [owner] The LifecycleOwner whose lifecycle will be observed.
 * @param [onCollect] The suspending lambda that will be invoked with each collected value.
 * @param [T] The type of values emitted by the Flow.
 */
inline fun <T> Flow<T>.collectOnLifecycleStarted(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        collect {
            onCollect(it)
        }
    }
}