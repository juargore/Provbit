import BrightCore
import ProvbitShared
import SwiftUI

extension EditTextViewData {
    func textBindingFrom() -> Binding<String> {
        Binding<String> {
            self.text
        } set: {
            self.onTextChanged($0)
        }
    }
}

struct EditTextView: View {
    @ObservedObject var component: AnyObservableViewData<EditTextViewData>

    var body: some View {
        if component.viewData.fieldType == EditTextViewData.FieldType.password {
            SecureField(
                component.viewData.label?.localized() ?? "",
                text: component.viewData.textBindingFrom()
            )
            .frame(height: 50, alignment: .center)
            .textFieldStyle(.roundedBorder)
            .padding([.leading, .trailing], 30)
        } else {
            TextField(
                component.viewData.label?.localized() ?? "",
                text: component.viewData.textBindingFrom()
            )
            .frame(height: 50, alignment: .center)
            .textFieldStyle(.roundedBorder)
            .padding([.leading, .trailing], 30)
            .foregroundColor(component.viewData.valid ? .black : .red)
        }
    }
}
