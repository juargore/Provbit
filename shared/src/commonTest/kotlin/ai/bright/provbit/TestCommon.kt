package ai.bright.provbit

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertFails


/**
 * Class from which other test classes should extend.
 *
 * Handles setting of main dispatcher so that `runTest` skips delays.
 */
@ExperimentalCoroutinesApi
open class BaseTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

/**
 * Test a state flow making sure there are no emissions after the given [validate] block.
 *
 * Note that this extension is only required because [StateFlow] is a hot flow. Cold flows will
 * by default fail if there are unhandled emissions.
 *
 * See https://github.com/cashapp/turbine#hot-flows.
 */
suspend fun <T> StateFlow<T>.testExhaustive(validate: suspend FlowTurbine<T>.() -> Unit) {
    test {
        validate(this)
        assertFails("Another ViewData was emitted when none was expected.") {
            val abc = awaitItem()
            println("Failed with $abc")
        }
    }
}
