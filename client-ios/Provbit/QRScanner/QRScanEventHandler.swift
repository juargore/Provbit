import ProvbitShared

final class QRScanEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `QRScanEvent`s.
    weak var coordinator: QRCoordinator?

    // MARK: Initialization

    /// Creates a new `QRScanEventHandler` using the provided `QRCoordinator`.
    init(coordinator: QRCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    func handle(event _: QRScanEvent) {}
}
