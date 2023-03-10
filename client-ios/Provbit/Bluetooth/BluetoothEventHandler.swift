import ProvbitShared

/// An object that is designed to handle all `BluetoothDemoEvent`s.
final class BluetoothEventHandler: EventHandler {
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
    func handle(event _: BluetoothScanEvent) {}
}
