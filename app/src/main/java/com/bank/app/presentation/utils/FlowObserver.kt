package com.bank.app.presentation.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowObserver<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {

    private var job: Job? = null

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect { collector(it) }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }
                else -> {
                }
            }
        })
    }
}


inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})

class FragmentObserver<T>(
    fragment: Fragment,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {
    init {
        fragment.viewLifecycleOwnerLiveData.observe(
            fragment,
            Observer { viewLifeCycleOwner ->
                FlowObserver(viewLifeCycleOwner, flow, collector)
            }
        )
    }
}

inline fun <reified T> Flow<T>.observe(
    fragment: Fragment,
    noinline collector: suspend (T) -> Unit
) = FragmentObserver(fragment, this, collector)

inline fun <reified T> Flow<T>.observeIn(
    fragment: Fragment
) = FragmentObserver(fragment, this, {})