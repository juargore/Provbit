import ProvbitShared
import SwiftUI

extension ToggleViewData {
    func boolBindingFrom() -> Binding<Bool> {
        Binding<Bool> {
            self.state
        } set: {
            self.set(KotlinBoolean(value: $0))
        }
    }
}

struct ToggleView: View {
    @ObservedObject var component: AnyObservableViewData<ToggleViewData>

    var body: some View {
        Toggle(
            component.viewData.label?.localized() ?? "",
            isOn: component.viewData.boolBindingFrom()
        ).animation(.default)
    }
}
