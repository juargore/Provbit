import ProvbitShared
import SwiftUI
import UIKit

// MARK: - AppInfoCoordinator

/// A protocol that manages displaying views from the `AppInfoView`.
protocol AppInfoCoordinator: AnyObject {
    var rootViewController: UIViewController { get }

    // MARK: Methods

    /// Shows the `AppInfoView`.
    func showAppInfoView()

    /// Instantiates the child coordinator to show a `ItemListView`.
    func showList()

    /// Instantiates the child coordinator to show a `MutliSelectorDemoView`.
    func showMultiSelector()

    /// Instantiates the child coordinator to show a `QRResultsView`.
    func showQRResults()

    /// Initializes a `StyleGuideView` and adds it to the navigation stack.
    func showStyleGuide()

    /// Shows the `BluetoothView`.
    func showBluetoothList()
}

final class DefaultAppInfoCoordinator: AppInfoCoordinator {
    typealias Services = HasSharedComponent

    let log = ProvbitLogger(object: DefaultAppInfoCoordinator.self)

    /// The `UINavigationController` passed in from a parent coordinator.
    private var navigationController: UINavigationController

    // MARK: Coordinators

    /// A child coordinator of type `ItemListCoordinator`.
    private var itemListCoordinator: ItemListCoordinator?

    /// The parent coordinator.
    weak var parentCoordinator: AppCoordinator?

    let services: Services

    /// A child coordinator of type `QRCoordinator`.
    var qrCoordinator: QRCoordinator?

    /// The root view controller.
    var rootViewController: UIViewController {
        navigationController
    }

    /// Creates an instance of `DefaultAppInfoCoordinator`.
    /// - Parameters:
    ///   - navigationController: The navigation controller passed in from the parent.
    ///   - parentCoordinator: The parent coordinator.
    init(services: Services, navigationController: UINavigationController, parentCoordinator: AppCoordinator) {
        self.services = services
        self.navigationController = navigationController
        self.parentCoordinator = parentCoordinator
    }

    // MARK: Navigation Methods

    /// A method to navigate to the `AppInfoView`.
    func showAppInfoView() {
        log.v { "showAppInfoView()" }
        let eventHandler = AppInfoEventHandler(coordinator: self)
        let processor = services.sharedComponent.appInfoProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = AppInfoView(viewModel: AnyObservableViewData(wrappedObservableViewData: viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.setViewControllers([viewController], animated: true)
    }

    /// Creates the child coordinator and presents a `ItemListView`.
    func showList() {
        itemListCoordinator = DefaultItemListCoordinator(services: services, parentCoordinator: self)
        itemListCoordinator?.showList()
    }

    /// Shows a `MultiSelectorDemoView`.
    func showMultiSelector() {
        let eventHandler = MultiSelectorDemoEventHandler(coordinator: self)
        let processor = services.sharedComponent.multiSelectorDemoProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = MultiSelectorDemoView(viewModel: AnyObservableViewData(wrappedObservableViewData: viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.setViewControllers([viewController], animated: true)
    }

    /// Creates the child coordinator and presents a `QRScanView`.
    func showQRResults() {
        qrCoordinator = DefaultQRCoordinator(services: services, parentCoordinator: self)
        qrCoordinator?.showQrScan()
    }

    /// Shows a `StyleGuideView`.
    func showStyleGuide() {
        let processor = services.sharedComponent.styleGuideProcessor()
        let eventHandler = StyleGuideEventHandler()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = StyleGuideView(component: AnyObservableViewData(wrappedObservableViewData: viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.pushViewController(viewController, animated: true)
    }

    func showBluetoothList() {
        let processor = services.sharedComponent.bluetoothScanProcessor()
        let eventHandler = BluetoothEventHandler(coordinator: self)
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = BluetoothView(component: AnyObservableViewData(wrappedObservableViewData: viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.pushViewController(viewController, animated: true)
    }
}
