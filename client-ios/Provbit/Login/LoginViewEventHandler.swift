import ProvbitShared

/// An object that is designed to handle all `LoginEvent`s.
final class LoginViewEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `LoginEvent`s.
    weak var coordinator: LoginCoordinator?

    // MARK: Initialization

    /// Creates a new `LoginViewEventHandler` using the provided `LoginCoordinator`.
    init(coordinator: LoginCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event: LoginEvent) {
        switch LoginEventKs(event) {
        case .toForm:
            coordinator?.showForm()
        }
    }
}
