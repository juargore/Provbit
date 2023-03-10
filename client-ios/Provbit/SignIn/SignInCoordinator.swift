import ProvbitShared
import SwiftUI

// MARK: - FormCoordinator

/// A protocol that manages displaying views from the `LoginView`.
protocol SignInCoordinator: AnyObject {
    // MARK: Methods

    /// Dismisses the `SignInView`.
    func signedIn()

    /// Shows the `SignInView`.
    func showSignIn()
}

// MARK: - DefaultSignInCoordinator

/// The default implementation of `SignInCoordinator`. Handles presenting and dismissing
/// the `SignInView`.
final class DefaultSignInCoordinator: SignInCoordinator {
    // MARK: Types

    /// Defines the services required for this coordinator.
    typealias Services = HasSharedComponent

    // MARK: Properties

    /// The `UINavigationController` for this coordinator.
    private let navigationController: UINavigationController

    /// The parent coordinator.
    weak var parentCoordinator: AppCoordinator?

    /// The services used by this coordinator.
    private var services: Services

    // MARK: Initialization

    /// Creates an instance of `DefaultFormCoordinator`.
    /// - Parameters:
    ///   - parentCoordinator: The parent coordinator.
    init(services: Services,
         navigationController: UINavigationController,
         parentCoordinator: AppCoordinator) {
        self.services = services
        self.navigationController = navigationController
        self.parentCoordinator = parentCoordinator
    }

    // MARK: Methods

    func showSignIn() {
        let eventHandler = SignInViewEventHandler(coordinator: self)
        let processor = services.sharedComponent.signInProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = SignInView(component: observe(viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.setViewControllers([viewController], animated: true)
    }

    func signedIn() {
        navigationController.popViewController(animated: true)
    }
}
