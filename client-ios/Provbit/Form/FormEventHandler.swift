import ProvbitShared

/// An object that is designed to handle all `FormEvent`s.
///
final class FormEventHandler: EventHandler {
    // MARK: Properties

    /// A coordinator used to coordinate navigation events triggered by `FormEvent`s.
    weak var coordinator: LoginCoordinator?

    // MARK: Initialization

    /// Creates a new `FormEventHandler` using the provided `LoginCoordinator`.
    init(coordinator: LoginCoordinator) {
        self.coordinator = coordinator
    }

    // MARK: Methods

    /// Handles the provided event.
    ///
    /// - parameter event: The event to handle.
    func handle(event: FormEvent) {
        switch FormEventKs(event) {
        case .submitForm:
            print("form submitted")
        }
    }
}
