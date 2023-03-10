package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.BaseTest
import ai.bright.provbit.architecture.ViewScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
open class BaseComponentTest : BaseTest(), ViewScope {
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main
}