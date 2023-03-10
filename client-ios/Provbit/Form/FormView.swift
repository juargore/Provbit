import ProvbitShared
import SwiftUI

// MARK: - FormView

func genBinder<V>(_ viewData: V) -> Binding<V> {
    Binding<V> {
        viewData
    } set: { _ in }
}

/// A `View` for displaying a form for the user to fill out.
struct FormView: View {
    @ObservedObject var component: AnyObservableViewData<FormViewData>

    var body: some View {
        print("Reval: FormView")
        return Form {
            EditTextView(component: observe(component.viewData.firstName))
            EditTextView(component: observe(component.viewData.lastName))
            EditTextView(component: observe(component.viewData.lastName))
            EditTextView(component: observe(component.viewData.phoneNumber))
            ToggleView(component: observe(component.viewData.isUsCitizen))
            //            Text(viewModel.viewData.buttonEnabled ? "Enabled" : "Disabled")
            //            Button(viewModel.viewData., action: <#T##() -> Void#>)
        }

        //        return FormInnerView(binding: $viewModel.viewData)
    }
}

#if DEBUG

    struct FormView_Previews: PreviewProvider {
        static var previews: some View {
            Group {
                NavigationView {
                    FormView(component: previewAnyObservable(
                        FormViewData(
                            emailAddress: editTextPreview("Email", content: "josh@example.com"),
                            firstName: editTextPreview("First", content: "Josh"),
                            lastName: editTextPreview("Last", content: "Lastname"),
                            isUsCitizen: togglePreview("Is US Citizen", state: true),
                            phoneNumber: editTextPreview("Phone Number", content: "555-555-1234"),
                            favoriteColor: selectorPreview(),
                            sliderValue: sliderPreview(),
                            formSubmitButtonClick: {}
                        )
                    ))
                    .navigationTitle("Form")
                    .navigationBarTitleDisplayMode(.inline)
                }
            }
        }
    }

#endif
