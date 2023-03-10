package ai.bright.provbit.architecture

import kotlinx.coroutines.flow.StateFlow

/**
 * The standard [Interactor] is defined in terms of a suspending invoke function.
 * This allows the asynchronous operation to be scheduled by the caller. In the case
 * of a [StateFlow] response, this is usually done on collection rather than at invocation
 * of the interactor function. [StateFlowInteractor] provides a method for returning an
 * [Out] [StateFlow] from a synchronous invocation.
 */
interface StateFlowInteractor<In, Out> {
    operator fun invoke(input: In): StateFlow<Out>
}
operator fun <Out> StateFlowInteractor<Unit, Out>.invoke(): StateFlow<Out> = invoke(Unit)
