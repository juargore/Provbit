import UIKit

extension UIApplication {
    /// Informs the application that all editing should end. Informs all `UIResponder`s to resign their first
    /// responder status.
    func endEditing() {
        sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
