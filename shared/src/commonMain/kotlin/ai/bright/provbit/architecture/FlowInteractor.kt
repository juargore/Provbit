package ai.bright.provbit.architecture

import kotlinx.coroutines.flow.Flow

/**
 * The standard [Interactor] is defined in terms of a suspending invoke function.
 * This allows the asynchronous operation to be scheduled by the caller. In the case
 * of a [Flow] response, this is usually done on collection rather than at invocation
 * of the interactor function. [FlowInteractor] provides a method for returning an
 * [Out] [Flow] from a synchronous invocation.
 */
interface FlowInteractor<In, Out> {
    operator fun invoke(input: In): Flow<Out>
}
suspend operator fun <Out> FlowInteractor<Unit, Out>.invoke(): Flow<Out> = invoke(Unit)
