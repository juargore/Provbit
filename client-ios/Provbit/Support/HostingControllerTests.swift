import SwiftUI
import XCTest

@testable import Provbit

class HostingControllerTests: XCTestCase {
    fileprivate var subject: HostingController<TestView>!

    /// `init(rootView:statusBarStyle:)` creates a hosting controller sets the preferredStatusBarStyle.
    func testPreferredStatusBarStyle() {
        subject = HostingController(rootView: TestView())
        XCTAssertEqual(subject.preferredStatusBarStyle, .darkContent)

        subject = HostingController(rootView: TestView(), statusBarStyle: .lightContent)
        XCTAssertEqual(subject.preferredStatusBarStyle, .lightContent)
    }

    /// `init(rootView:ignoresSafeArea:)` creates a hosting controller that ignores the safe areas.
    func testIgnoresSafeArea() {
        subject = HostingController(rootView: TestView(), ignoreSafeArea: true)
        setKeyWindowRoot(viewController: subject)

        XCTAssertEqual(subject.view?.safeAreaInsets, .zero)

        NotificationCenter.default.post(
            name: UIResponder.keyboardWillShowNotification,
            object: nil,
            userInfo: [
                UIResponder.keyboardAnimationDurationUserInfoKey: TimeInterval(0),
                UIResponder.keyboardFrameEndUserInfoKey: CGRect(x: 0, y: 0, width: 300, height: 300),
            ]
        )
        XCTAssertEqual(subject.view?.safeAreaInsets, .zero)
    }
}

private struct TestView: View, HasPreferredStatusBarStyle {
    var body: some View {
        Text("✂️")
    }

    var preferredStatusBarStyle: UIStatusBarStyle {
        .darkContent
    }
}
