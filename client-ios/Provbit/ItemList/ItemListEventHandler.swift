import ProvbitShared

/// An object that is designed to handle all `ItemListEvent`s.
final class ItemListEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `ItemListEvent`s.
    weak var coordinator: ItemListCoordinator?

    // MARK: Initialization

    /// Creates a new `ItemListEventHandler` using the provided `ItemListCoordinator`.
    init(coordinator: ItemListCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event: ItemListEvent) {
        switch ItemListEventKs(event) {
        case let .toDetail(item):
            coordinator?.showListDetail(item: item)
        }
    }
}
