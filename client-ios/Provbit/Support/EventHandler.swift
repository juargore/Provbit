import Foundation
import ProvbitShared

// MARK: - EventHandler

/// A protocol defining a type of object that handles all `Event` objects passed to a `ViewModel`.
protocol EventHandler {
    // MARK: Types

    /// The type of `Event` used in this `EventHandler`.
    associatedtype Event

    // MARK: Methods

    /// Handles the provided event. Typically this will be some navigation command that should be executed.
    ///
    /// - parameter event: The event to handle.
    func handle(event: Event)
}
