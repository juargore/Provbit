import ProvbitShared

/// An object that is designed to handle all `LoginEvent`s.
final class SignInViewEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `LoginEvent`s.
    weak var coordinator: SignInCoordinator?

    // MARK: Initialization

    /// Creates a new `LoginViewEventHandler` using the provided `LoginCoordinator`.
    init(coordinator: SignInCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event: SignInEvent) {
        switch SignInEventKs(event) {
        case .dismiss:
            coordinator?.signedIn()
        }
    }
}
