import ProvbitShared

/// An object that is designed to handle all `ItemDetailEvent`s.
final class ItemListDetailEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `ItemDetailEvent`s.
    weak var coordinator: ItemListCoordinator?

    // MARK: Initialization

    /// Creates a new `ItemDetailEvent` using the provided `ItemListCoordinator`.
    init(coordinator: ItemListCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event: ItemDetailEvent) {
        switch ItemDetailEventKs(event) {
        case .back:
            return
        }
    }
}
