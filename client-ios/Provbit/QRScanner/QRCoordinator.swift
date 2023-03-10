import AVKit
import ProvbitShared
import SwiftUI
import UIKit

// MARK: - QRCoordinator

/// A protocol that manages displaying views from the `QRResultsView`.
protocol QRCoordinator: AnyObject {
    // MARK: Methods

    /// Shows the qr scan view.
    func showQrScan()
}

// MARK: - DefaultQRCoordinator

final class DefaultQRCoordinator: QRCoordinator {
    typealias Services = HasSharedComponent

    /// The `UINavigationController` for this coordinator.
    let navigationController = UINavigationController()

    /// The services used by this `Coordinator`.
    let services: Services

    /// The parent coordinator.
    weak var parentCoordinator: AppInfoCoordinator?

    /// - Parameters:
    ///   - services: The services used by this `Coordinator`.
    ///   - parentCoordinator: The parent coordinator.
    init(services: Services, parentCoordinator: AppInfoCoordinator) {
        self.services = services
        self.parentCoordinator = parentCoordinator
    }

    // MARK: Navigation Methods

    /// A method to navigate to a sample `QRScanView`.
    func showQrScan() {
        let eventHandler = QRScanEventHandler(coordinator: self)
        let processor = services.sharedComponent.qrScanProcessor()
        let viewModel = ViewModel(processor: processor, eventHandler: eventHandler)
        let view = QRScanView(component: observe(viewModel))
        let viewController = HostingController(rootView: view)
        navigationController.setViewControllers([viewController], animated: true)
        parentCoordinator?.rootViewController.present(navigationController, animated: true)
    }
}
