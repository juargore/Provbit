package ai.bright.provbit.architecture

/**
 * Result is a value that represents either success or failure.
 *
 * Result is templated on both an Err type and an Ok type.
 *
 * For general info: https://en.wikipedia.org/wiki/Result_type
 *
 * One helpful way of using a [Result] is like this (where someFunction
 * is returning either an int error code or a success string).
 *
 * val result: Result<Int, String> = someFunction()
 * when (result) {
 *     is Result.Err -> Log.e("Some function failed with code: ${result.error}")
 *     is Result.Ok -> Log.i("Some function succeeded with result: ${result.value}")
 * }
 *
 * Note that Kotlin smart-casting lets you access error/value based on the branch.
 *
 * In many cases, the error state may simply be a Throwable. For those cases
 * check out [ThrowableResult]. This is analogous to the Kotlin builtin [Result].
 *
 */
sealed class Result<out E, out V> {

    fun isOk(): Boolean = this is Ok
    fun isErr(): Boolean = this is Err

    data class Ok<out V>(val value: V) : Result<Nothing, V>()
    data class Err<out E>(val error: E) : Result<E, Nothing>()

    companion object {
        fun unitOk() = Unit.asOk()
        fun unitErr() = Unit.asErr()
    }
}

fun <E, V> Result<E, V>.okOr(default: V): V = when (this) {
    is Result.Err -> default
    is Result.Ok -> value
}

fun <E, V> Result<E, V>.okOrNull(): V? = when (this) {
    is Result.Err -> null
    is Result.Ok -> value
}

fun <E, V> Result<E, V>.assertOk(): V = when (this) {
    is Result.Err -> throw AssertionError("Result was Err")
    is Result.Ok -> value
}

fun <E, V> Result<E, V>.errOr(default: E): E = when (this) {
    is Result.Err -> error
    is Result.Ok -> default
}

fun <E, V> Result<E, V>.errOrNull(): E? = when (this) {
    is Result.Err -> error
    is Result.Ok -> null
}

fun <E, V> Result<E, V>.assertErr(): E = when (this) {
    is Result.Err -> error
    is Result.Ok -> throw AssertionError("Result was Ok")
}

fun <V> V.asOk(): Result<Nothing, V> = Result.Ok(this)

fun <E> E.asErr(): Result<E, Nothing> = Result.Err(this)

inline fun <E, V, R> Result<E, V>.map(transform: (V) -> R): Result<E, R> = when (this) {
    is Result.Err -> this
    is Result.Ok -> transform(value).asOk()
}

inline fun <E, V, R> Result<E, V>.andThen(transform: (V) -> Result<E, R>): Result<E, R> = when (this) {
    is Result.Err -> this
    is Result.Ok -> transform.invoke(value)
}

inline fun <E, V, R> Result<E, V>.fold(
    onErr: (E) -> R,
    onOk: (V) -> R
): R = when (this) {
    is Result.Err -> onErr(error)
    is Result.Ok -> onOk(value)
}

inline fun <E, V> Result<E, V>.onOk(onOk: (V) -> Unit): Result<E, V> = this.on({}, onOk)

inline fun <E, V> Result<E, V>.onErr(onErr: (E) -> Unit): Result<E, V> = this.on(onErr, {})

inline fun <E,V> Result<E, V>.on(onErr: (E) -> Unit, onOk:(V) -> Unit): Result<E, V> = apply {
    when (this) {
        is Result.Ok -> onOk(value)
        is Result.Err -> onErr(error)
    }
}

typealias ThrowableResult<V> = Result<Throwable, V>

fun <V> ThrowableResult<V>.okOrThrow(): V = when (this) {
    is Result.Err -> throw error
    is Result.Ok -> value
}

inline fun <V> catchThrownResult(block: () -> V): ThrowableResult<V> {
    return try {
        Result.Ok(block())
    } catch (e: Throwable) {
        Result.Err(e)
    }
}

inline fun <V> catchResult(block: () -> V): Result<Unit, V> {
    return try {
        Result.Ok(block())
    } catch (e: Throwable) {
        Result.unitErr()
    }
}

inline fun <E, V> catchResult(failureMap: (Throwable) -> E, block: () -> V): Result<E, V> {
    return try {
        Result.Ok(block())
    } catch (e: Throwable) {
        Result.Err(failureMap(e))
    }
}
