package ai.bright.provbit.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 * Returns a new [StateFlow] with the given [transform] applied
 * to the initial states of [flow1] and [flow2]
 */
fun <T1, T2, R> CoroutineScope.combine(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    transform: (T1, T2) -> R
): StateFlow<R> = kotlinx.coroutines.flow.combine(
    flow1, flow2
) { t1, t2 -> transform(t1, t2) }
    .stateIn(this, SharingStarted.Eagerly, transform(flow1.value, flow2.value))

/**
 * Returns a new [StateFlow] with the given [transform] applied
 * to the initial states of [flow1], [flow2], and [flow3]
 */
fun <T1, T2, T3, R> CoroutineScope.combine(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    transform: (T1, T2, T3) -> R
): StateFlow<R> = kotlinx.coroutines.flow.combine(
    flow1, flow2, flow3
) { t1, t2, t3 -> transform(t1, t2, t3) }
    .stateIn(this, SharingStarted.Eagerly, transform(flow1.value, flow2.value, flow3.value))

/**
 * etc.
 */
fun <T1, T2, T3, T4, R> CoroutineScope.combine(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    transform: (T1, T2, T3, T4) -> R
): StateFlow<R> = kotlinx.coroutines.flow.combine(
    flow1, flow2, flow3, flow4
) { t1, t2, t3, t4 -> transform(t1, t2, t3, t4) }
    .stateIn(this, SharingStarted.Eagerly, transform(flow1.value, flow2.value, flow3.value, flow4.value))

/**
 * etc.
 */
fun <T1, T2, T3, T4, T5, R> CoroutineScope.combine(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    flow5: StateFlow<T5>,
    transform: (T1, T2, T3, T4, T5) -> R
): StateFlow<R> = kotlinx.coroutines.flow.combine(
    flow1, flow2, flow3, flow4, flow5
) { t1, t2, t3, t4, t5 -> transform(t1, t2, t3, t4, t5) }
    .stateIn(this, SharingStarted.Eagerly, transform(
        flow1.value, flow2.value, flow3.value, flow4.value, flow5.value))

/**
 * Once you get to the point that combine doesn't support types,
 * you have to do it yourself. This isn't as terrible as it looks.
 */
fun <T1, T2, T3, T4, T5, T6, T7, R> CoroutineScope.combine(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    flow5: StateFlow<T5>,
    flow6: StateFlow<T6>,
    flow7: StateFlow<T7>,
    transform: (T1, T2, T3, T4, T5, T6, T7) -> R
): StateFlow<R> = kotlinx.coroutines.flow.combine(
    flow1,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
    flow7
) {
    transform(
        it[0] as T1,
        it[1] as T2,
        it[2] as T3,
        it[3] as T4,
        it[4] as T5,
        it[5] as T6,
        it[6] as T7,
    )
}.stateIn(this, SharingStarted.Eagerly,
    transform(
        flow1.value,
        flow2.value,
        flow3.value,
        flow4.value,
        flow5.value,
        flow6.value,
        flow7.value
    )
)

/**
 * Creates a state flow using an initial [value] and a suspend
 * block for the creation of new flow for subsequent values.
 * This is useful when you want to setup a [StateFlow] that will be
 * updated by subsequent suspend calls that can all be setup in [block]
 */
fun <T1> CoroutineScope.initialCommonStateFlow(
    value: T1,
    block: suspend FlowCollector<T1>.() -> Unit
): CommonStateFlow<T1> = flow(block).stateIn(this, SharingStarted.Eagerly, value).asCommonStateFlow()

/**
 * Similar to [combine] but for single value [StateFlow].
 * Creates a state flow using an initial [flow] and a transform
 * block for mapping.
 */
fun <T1, R> map(
    flow: StateFlow<T1>,
    transform: (T1) -> R
): StateFlow<R> = MappedStateFlow(flow, transform)

class MappedStateFlow<T, R>(private val source: StateFlow<T>, private val mapper: (T) -> R) : StateFlow<R> {

    override val value: R
        get() = mapper(source.value)

    override val replayCache: List<R>
        get() = source.replayCache.map(mapper)

    override suspend fun collect(collector: FlowCollector<R>) : Nothing {
        source.collect { value -> collector.emit(mapper(value)) }
    }
}