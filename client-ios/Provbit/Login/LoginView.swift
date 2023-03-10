import ProvbitShared
import SwiftUI

struct LoginView: View {
    @ObservedObject var component: AnyObservableViewData<LoginViewData>

    @State private var email: String = ""
    @State private var password: String = ""

    var body: some View {
        VStack(alignment: .center, spacing: 20) {
            TextField("Email", text: $email)
                .frame(height: 50, alignment: .center)
                .textFieldStyle(.roundedBorder)
                .padding([.leading, .trailing], 30)
                .keyboardType(.emailAddress)
            SecureField("Password", text: $password)
                .frame(height: 50, alignment: .center)
                .textFieldStyle(.roundedBorder)
                .padding([.leading, .trailing], 30)
            ButtonView(state: component.viewData.registerButton)
            ButtonView(state: component.viewData.loginButton)
            Spacer()
        }
        .buttonStyle(ProvbitButtonStyle())
        .navigationTitle("Login")
    }
}

#if DEBUG

//    struct LoginView_Previews: PreviewProvider {
//        static var previews: some View {
//            Group {
//                NavigationView {
//                    LoginView(
//                        viewModel: PreviewCommonState(
//                            viewData: LoginViewData(
//                                email: editTextPreview("Email", content: "josh@example.com"),
//                                password: editTextPreview("Password", content: ""),
//                                registerButton: buttonPreview("Register"),
//                                loginButton: buttonPreview("Login")
//                            )
//                        )
//                    ).navigationBarTitleDisplayMode(.inline)
//                }
//            }
//        }
//    }

#endif
