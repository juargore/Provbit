import ProvbitShared

/// An object that is designed to handle all `AppInfoEvent`s.
final class AppInfoEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `AppInfoEvent`s.
    weak var coordinator: AppInfoCoordinator?

    // MARK: Initialization

    /// Creates a new `AppInfoEventHandler` using the provided `AppInfoCoordinator`.
    init(coordinator: AppInfoCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event: AppInfoEvent) {
        switch AppInfoEventKs(event) {
        case .toList:
            coordinator?.showList()
        case .toQRResults:
            coordinator?.showQRResults()
        case .toSelector:
            coordinator?.showMultiSelector()
        case .toStyleGuide:
            coordinator?.showStyleGuide()
        case .toImageAnalysis:
            return
        case .toBluetooth:
            coordinator?.showBluetoothList()
        }
    }
}
