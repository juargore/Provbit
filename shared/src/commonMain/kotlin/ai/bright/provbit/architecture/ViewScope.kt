package ai.bright.provbit.architecture

import kotlinx.coroutines.CoroutineScope

/**
 * [ViewScope] is simply a [CoroutineScope], and is used to
 * avoid cluttering the global [CoroutineScope] space.
 *
 * Objects that implement [ViewScope] will by convention be
 * parented by a [Processor], which is used by native
 * views.
 */
interface ViewScope: CoroutineScope