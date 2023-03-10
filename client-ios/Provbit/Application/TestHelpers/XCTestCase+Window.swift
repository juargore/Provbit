import XCTest

extension XCTestCase {
    /// Make a `UIViewController` the root view controller in the test window. Allows testing
    /// changes to the navigation stack when they would ordinarily be invisible to the testing
    /// environment.
    ///
    /// - Parameters:
    ///     - viewController: The `UIViewController` to make root view controller.
    ///     - window: An optional parameter for setting the window of the view controller. This is most commonly
    ///               used to override the window frame.
    ///
    func setKeyWindowRoot(
        viewController: UIViewController,
        window: UIWindow = UIApplication.shared.windows.first { $0.isKeyWindow }!
    ) {
        window.rootViewController = viewController
        window.makeKeyAndVisible()
    }

    /// Nests a `UIView` within a root view controller in the test window. Allows testing
    /// changes to the view that require the view to exist within a window or are dependent on safe
    /// area layouts.
    ///
    /// - Parameters:
    ///     - view: The `UIView` to add to a root view controller.
    ///     - window: An optional parameter for setting the window of the view. This is most commonly
    ///               used to override the window frame.
    ///
    func setKeyWindowRoot(
        view: UIView,
        window: UIWindow = UIApplication.shared.windows.first { $0.isKeyWindow }!
    ) {
        let viewController = UIViewController()
        view.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.addSubview(view)
        view.topAnchor.constraint(equalTo: viewController.view.topAnchor, constant: 0).isActive = true
        view.leadingAnchor.constraint(equalTo: viewController.view.leadingAnchor, constant: 0).isActive = true
        view.bottomAnchor.constraint(equalTo: viewController.view.bottomAnchor, constant: 0).isActive = true
        view.trailingAnchor.constraint(equalTo: viewController.view.trailingAnchor, constant: 0).isActive = true
        window.rootViewController = viewController
        window.makeKeyAndVisible()
    }
}
