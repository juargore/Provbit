import UIKit

/// A view controller with the sole purpose of directing which view controller manages the
/// view controller's navigation controller's status bar.
///
class StatusBarForwardingViewController: UIViewController {
    /// The view controller to use for the status bar.
    var statusBarViewController: UIViewController? {
        didSet {
            setNeedsStatusBarAppearanceUpdate()
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .systemBlue
    }

    override var preferredStatusBarStyle: UIStatusBarStyle { .lightContent }

    override var childForStatusBarStyle: UIViewController? {
        statusBarViewController
    }

    override var childForStatusBarHidden: UIViewController? {
        statusBarViewController
    }
}

/// A navigation controller with the sole purpose of directing which view controller manages the
/// status bar style.
///
class NavigationController: UINavigationController {
    override var childForStatusBarStyle: UIViewController? {
        if let presented = presentedViewController, !presented.isBeingDismissed {
            return presented
        } else {
            return topViewController
        }
    }
}
