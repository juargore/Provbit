package ai.bright.provbit.architecture

/**
 * Many times, the output of an [Interactor] can be either successful or failure. Given
 * the flexibility of the [Interactor] type, these can be defined many ways, but there are
 * three common methods:
 * 1. Use of the builtin Kotlin Result Type
 * 2. Use of a custom sealed type
 * 3. Use of a custom Result type.
 *
 * The downsides of option 1 is that the failure case is not bounded. That is, the failure
 * mode returns simply a Throwable, with no limitation.
 *
 * Option 2 precludes using helpful result composition functions such as `andThen`, `map`,
 * `fold`, etc, and requires users to breakdown the response using a custom `when` block.
 *
 * This interface provides an [Interactor] defined in terms of a common [Either] type, which
 * provides strict bounds on [Failure] types while maintaining a set of helpful composition
 * functions.
 *
 * In general, it is best practice for [Interactor] classes to never throw on invocation.
 */
interface ResultInteractor<In, Failure, Success>: Interactor<In, Result<Failure, Success>>