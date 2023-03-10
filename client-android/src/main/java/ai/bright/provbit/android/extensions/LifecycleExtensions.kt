package ai.bright.provbit.android.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Observe flow changes in a fragment/activity lifecycle.
 */
fun <T> Flow<T>.observe(owner: LifecycleOwner, action: suspend (T) -> Unit) =
    owner.lifecycleScope.launchWhenCreated {
        collectLatest(action)
    }