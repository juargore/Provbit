package ai.bright.provbit.architecture

/**
 * An [Interactor] is the basic working unit of the domain layer.
 *
 * An interactor is a defined function with an [In] and [Out], that's always
 * run suspend.
 *
 * The advantage to defining an Interactor rather than a simple function
 * is that Interactors have constructors, which means they have dependencies.
 * Usually this will a Port, but it can be another Interactor for easy
 * composition.
 */
interface Interactor<In, Out> {
    suspend operator fun invoke(input: In): Out
}

suspend operator fun <Out> Interactor<Unit, Out>.invoke(): Out = invoke(Unit)