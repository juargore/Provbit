@file:Suppress("IllegalIdentifier")

package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.architecture.MappedStateFlow
import ai.bright.provbit.architecture.map
import ai.bright.provbit.testExhaustive
import app.cash.turbine.test
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EditTextComponentTests : BaseComponentTest() {

    @Test
    fun `when onTextChanged is called the text updates`() = runTest {
        val component = editTextComponent("")
        component.testExhaustive {
            val initial = awaitItem()
            assertEquals("", initial.text)
            val updatedText = "input"
            initial.onTextChanged(updatedText)
            assertEquals(updatedText, awaitItem().text)
        }
    }

    @Test
    fun `default values are correct`() = runTest {
        val label = "label".desc()
        val hint = "hint".desc()
        val valid = false
        val fieldType = EditTextViewData.FieldType.Password
        val component = editTextComponent(
            initialText = "foobar",
            label = label,
            hint = hint,
            fieldType = fieldType,
            validationCheck = { valid }
        )
        component.testExhaustive {
            val initial = awaitItem()
            println("Got $initial")
            assertEquals(initial.label, label)
            assertEquals(initial.hint, hint)
            assertEquals(initial.valid, valid)
            assertEquals(initial.fieldType, fieldType)
        }
    }
}

