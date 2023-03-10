import ProvbitShared
import SwiftUI
import UIKit

// MARK: - FormCoordinator

/// A protocol that manages displaying views from the `LoginView`.
protocol LoginCoordinator: AnyObject {
    // MARK: Methods

    /// Dismisses the `FormView`.
    func submitForm()

    /// Shows the form view.
    func showForm()

    /// Shows the `LoginView`
    func showLogin()
}

final class DefaultLoginCoordinator: LoginCoordinator {
    typealias Services = HasSharedComponent

    /// The `UINavigationController` for this coordinator.
    private let navigationController: UINavigationController

    /// The parent coordinator.
    weak var parentCoordinator: AppCoordinator?

    private var services: Services

    /// Creates an instance of `DefaultFormCoordinator`.
    /// - Parameters:
    ///   - parentCoordinator: The parent coordinator.
    init(
        services: Services,
        navigationController: UINavigationController,
        parentCoordinator: AppCoordinator
    ) {
        self.services = services
        self.navigationController = navigationController
        self.parentCoordinator = parentCoordinator
    }

    // MARK: Navigation Methods

    /// A method to navigate to a sample `FormView`.
    func showForm() {
        let eventHandler = FormEventHandler(coordinator: self)
        let processor = services.sharedComponent.formProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = FormView(component: observe(viewModel))
        let viewController = HostingController(rootView: view)
        viewController.title = "Form"
        navigationController.pushViewController(viewController, animated: true)
    }

    /// A method to navigate to a sample `LoginView`.
    func showLogin() {
        print("Running show login")
        let eventHandler = LoginViewEventHandler(coordinator: self)
        let processor = services.sharedComponent.loginProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = LoginView(component: observe(viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.setViewControllers([viewController], animated: false)
    }

    /// A method to dismiss the `FormView`.
    func submitForm() {
        navigationController.popViewController(animated: true)
    }
}
