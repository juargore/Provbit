package ai.bright.provbit.demo.presentation

import ai.bright.provbit.MR
import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.domain.uses.user.SignInUseCase
import ai.bright.provbit.demo.presentation.components.*
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.*
import me.tatarka.inject.annotations.Inject

data class FormViewData(
    val emailAddress: CommonStateFlow<EditTextViewData>,
    val firstName: CommonStateFlow<EditTextViewData>,
    val lastName: CommonStateFlow<EditTextViewData>,
    val isUsCitizen: CommonStateFlow<ToggleViewData>,
    val phoneNumber: CommonStateFlow<EditTextViewData>,
    val favoriteColor: CommonStateFlow<SelectorViewData>,
    val sliderValue: CommonStateFlow<SliderViewData>,
    val formSubmitButtonClick: () -> Unit
)

sealed class FormEvent {
    object SubmitForm : FormEvent()
}

enum class FavoriteColors {
    Red,
    Green,
    Blue,
    Yellow,
    Unlisted
}

@Inject
class FormProcessor(
    private val signInUseCase: SignInUseCase,
): BaseProcessor<FormViewData, FormEvent>() {

    private val emailAddressEdit = editTextComponent(label = MR.strings.email_label.desc())
    private val firstNameEdit = editTextComponent(label = MR.strings.first_name_label.desc())
    private val lastNameEdit = editTextComponent(label = MR.strings.last_name_label.desc())
    private val isUsCitizenToggle = toggleComponent(true, label = MR.strings.tap_to_count.desc())
    private val phoneNumberEdit = editTextComponent(label = MR.strings.phone_number_label.desc()) { it.length <= 10 }
    private val colors = FavoriteColors.values().map(FavoriteColors::name)
    private val colorSelector = selectorComponent(colors)
    private val sliderSlider = sliderComponent(0, 1, -10..10)

    override val viewData = CommonStateFlow.singleton(
        FormViewData(
            emailAddressEdit,
            firstNameEdit,
            lastNameEdit,
            isUsCitizenToggle,
            phoneNumberEdit,
            colorSelector,
            sliderSlider,
            formSubmitButtonClick = { launch { signInUseCase(emailAddressEdit.value.text) } },
        )
    )
}