import ProvbitShared
import SwiftUI
import UIKit

// MARK: - ItemListCoordinator

/// A protocol that manages displaying views from the `ItemListView`.
protocol ItemListCoordinator: AnyObject {
    // MARK: Methods

    /// Shows the list view.
    func showList()

    /// Shows the list detail view.
    func showListDetail(item: ItemListEvent.ToDetail)

    /// Navigates to the previous screen.
    func navigateBack()
}

final class DefaultItemListCoordinator: ItemListCoordinator {
    typealias Services = HasSharedComponent

    /// The `UINavigationController` for this coordinator.
    private(set) var navigationController = UINavigationController()

    let services: Services

    /// The parent coordinator.
    weak var parentCoordinator: AppInfoCoordinator?

    /// Creates an instance of `DefaultItemListCoordinator`.
    /// - Parameters:
    ///   - parentCoordinator: The parent coordinator.
    init(services: Services, parentCoordinator: AppInfoCoordinator) {
        self.services = services
        self.parentCoordinator = parentCoordinator
    }

    // MARK: Navigation Methods

    /// Called when a user clicks the Save button to update a `RealmItem`.
    func navigateBack() {
        navigationController.popViewController(animated: true)
    }

    /// A method to navigate to a sample `ItemListView`.
    func showList() {
        let eventHandler = ItemListEventHandler(coordinator: self)
        let processor = services.sharedComponent.itemListProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = ItemListView(viewModel: AnyObservableViewData(wrappedObservableViewData: viewModel))
        let viewController = HostingController(rootView: view)
        viewController.title = "Item List"
        navigationController.setViewControllers([viewController], animated: true)
        parentCoordinator?.rootViewController.present(navigationController, animated: true)
    }

    /// A method to navigate to a sample `ItemListDetailView`.
    func showListDetail(item: ItemListEvent.ToDetail) {
        let eventHandler = ItemListDetailEventHandler(coordinator: self)
        let processor = services.sharedComponent.itemDetailProcessor(item.itemGuid)
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = ItemListDetailView(viewModel: AnyObservableViewData(wrappedObservableViewData: viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.pushViewController(viewController, animated: true)
    }
}
