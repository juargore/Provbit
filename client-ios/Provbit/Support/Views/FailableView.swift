import SwiftUI

/// A SwiftUI `View` intended to be used as a view that will fail when instantiated.
/// ie. This view is created in the `.android` QRScanViewDataKs case.
struct FailableView: View {
    init() {
        assertionFailure()
    }

    var body: some View {
        EmptyView()
    }
}
