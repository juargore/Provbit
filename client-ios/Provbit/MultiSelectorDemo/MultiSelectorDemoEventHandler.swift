import ProvbitShared

/// An object that is designed to handle all `MultiSelectorDemoEvent`s.
final class MultiSelectorDemoEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `MultiSelectorDemoEvent`s.
    weak var coordinator: AppInfoCoordinator?

    // MARK: Initialization

    /// Creates a new `MultiSelectorDemoEventHandler` using the provided `AppInfoCoordinator`.
    init(coordinator: AppInfoCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event _: MultiSelectorDemoEvent) {}
}
