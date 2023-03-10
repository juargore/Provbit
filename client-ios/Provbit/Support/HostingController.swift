import SwiftUI

/// A protocol for a SwiftUI view to specify a status bar style.
///
protocol HasPreferredStatusBarStyle: SwiftUI.View {
    /// Preferred style of the status bar.
    var preferredStatusBarStyle: UIStatusBarStyle { get }
}

/// A `UIHostingController` that provides a way to ignore the safeArea and set the preferredStatusBarStyle.
class HostingController<Content>: UIHostingController<Content> where Content: View {
    // MARK: Properties

    /// Preferred style of the status bar.
    var statusBarStyle: UIStatusBarStyle = .lightContent

    @objc override var preferredStatusBarStyle: UIStatusBarStyle { statusBarStyle }

    // MARK: Initializers

    public init(rootView: Content, ignoreSafeArea: Bool = false, statusBarStyle: UIStatusBarStyle = .lightContent) {
        self.statusBarStyle = statusBarStyle
        super.init(rootView: rootView)
        if ignoreSafeArea {
            disableSafeArea()
        }
    }

    public init(rootView: Content, ignoreSafeArea: Bool = false) where Content: HasPreferredStatusBarStyle {
        statusBarStyle = rootView.preferredStatusBarStyle
        super.init(rootView: rootView)
        if ignoreSafeArea {
            disableSafeArea()
        }
    }

    // MARK: Methods

    override func viewDidLoad() {
        super.viewDidLoad()
        setNeedsStatusBarAppearanceUpdate()
    }

    @available(*, unavailable) @objc @MainActor dynamic required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented: \(aDecoder)")
    }

    /// Disables the hosting controller from receiving updates to its safe area.
    /// https://steipete.com/posts/disabling-keyboard-avoidance-in-swiftui-HostingController/
    /// https://gist.github.com/steipete/da72299613dcc91e8d729e48b4bb582c#file-HostingController-keyboard-swift
    ///
    private func disableSafeArea() {
        guard let viewClass = object_getClass(view) else { return }

        let viewSubclassName = String(cString: class_getName(viewClass)).appending("_IgnoreSafeArea")
        if let viewSubclass = NSClassFromString(viewSubclassName) {
            object_setClass(view, viewSubclass)
        } else {
            guard let viewClassNameUtf8 = (viewSubclassName as NSString).utf8String else { return }
            guard let viewSubclass = objc_allocateClassPair(viewClass, viewClassNameUtf8, 0) else { return }

            if let method = class_getInstanceMethod(UIView.self, #selector(getter: UIView.safeAreaInsets)) {
                let safeAreaInsets: @convention(block) (AnyObject) -> UIEdgeInsets = { _ in .zero }
                class_addMethod(
                    viewSubclass,
                    #selector(getter: UIView.safeAreaInsets),
                    imp_implementationWithBlock(safeAreaInsets),
                    method_getTypeEncoding(method)
                )
            }

            if let method2 = class_getInstanceMethod(
                viewClass,
                NSSelectorFromString("keyboardWillShowWithNotification:")
            ) {
                let keyboardWillShow: @convention(block) (AnyObject, AnyObject) -> Void = { _, _ in }
                class_addMethod(
                    viewSubclass,
                    NSSelectorFromString("keyboardWillShowWithNotification:"),
                    imp_implementationWithBlock(keyboardWillShow),
                    method_getTypeEncoding(method2)
                )
            }

            objc_registerClassPair(viewSubclass)
            object_setClass(view, viewSubclass)
        }
    }
}
