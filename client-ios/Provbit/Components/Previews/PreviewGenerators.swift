import ProvbitShared

func editTextPreview(
    _ fieldName: String,
    content: String = ""
) -> CommonStateFlow<EditTextViewData> {
    TestCommonState(viewData: EditTextViewData(
        text: content,
        label: PreviewStringDesc(fieldName),
        hint: nil,
        valid: true,
        fieldType: EditTextViewData.FieldType.text,
        onTextChanged: { _ in }
    )).viewData
}

func togglePreview(
    _ fieldName: String,
    state: Bool
) -> CommonStateFlow<ToggleViewData> {
    TestCommonState(viewData: ToggleViewData(
        state: state,
        label: PreviewStringDesc(fieldName),
        set: { _ in }
    )).viewData
}

func selectorPreview(
) -> CommonStateFlow<SelectorViewData> {
    TestCommonState(viewData: SelectorViewData(
        allElements: [],
        selectionIdx: 1,
        select: { _ in }
    )).viewData
}

func sliderPreview(
) -> CommonStateFlow<SliderViewData> {
    TestCommonState(viewData: SliderViewData(
        currentValue: 1,
        stepValue: 1,
        range: KotlinIntRange(start: 0, endInclusive: 10),
        setTo: { _ in }
    )).viewData
}

func buttonPreview(
    _ text: String
) -> ButtonViewData {
    ButtonViewData(
        content: PreviewStringDesc(text),
        style: ProvbitButtonType.Primary(),
        onClick: {}
    )
}

class PreviewStringDesc: ResourcesStringDesc {
    var content: String

    func localized() -> String {
        content
    }

    init(_ content: String) {
        self.content = content
    }
}
