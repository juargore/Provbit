@file:Suppress("IllegalIdentifier")
package ai.bright.provbit

import kotlin.test.Test
import kotlin.test.assertEquals

class GreeterTest {

    @Test
    fun `hello world greeting works as expected`() {
        assertEquals(
            expected = "Hello world, this is KMM.",
            actual = Greeter().greet("world")
        )
    }

    @Test
    fun `empty string greeting works as expected`() {
        assertEquals(
            expected = "Hello, this is KMM.",
            actual = Greeter().greet("")
        )
    }
}