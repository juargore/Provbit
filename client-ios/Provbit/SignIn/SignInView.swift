import ProvbitShared
import SwiftUI

// MARK: - SignInView

/// A view used to sign into the app using Auth0.
struct SignInView: View {
    @ObservedObject var component: AnyObservableViewData<SignInViewData>

    var body: some View {
        VStack(spacing: 16) {
            switch SignInViewDataLoginStateKs(component.viewData.loginState) {
            case .auth:
                Button {
                    component.viewData.logout()
                } label: {
                    Text("Logout")
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(16)
                }
            case .unauth:
                Button {
                    component.viewData.login()
                } label: {
                    Text("Sign In")
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(16)
                }
            }
            Button {
                component.viewData.queryForHardware()
            } label: {
                Text("Query for hardware")
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(16)
            }
            Text(component.viewData.hardwareSerial)
        }
    }
}
