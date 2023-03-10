import Foundation
import ProvbitShared

/// Protocol for an object that provides the component object defined by Kotlin Multiplatform
///
protocol HasSharedComponent {
    /// An instance of the SharedComponent. There should only ever be one of these.
    var sharedComponent: SharedComponent { get }
}

/// Typealias for the services provided by the `ServiceContainer`.
///
private typealias Services = HasSharedComponent

/// Object containing a list of services used by the app. This provides a single container that can
/// be injected to any coordinator that creates processors that need to access services. Processors
/// should declare which services they need access to by defining a typealias of any service
/// protocols for the services it needs to access. This allows for dependencies to be easily
/// injected and passed through the app while also limiting the processors to only accessing the
/// services that it needs.
///
class ServiceContainer: Services {
    let sharedComponent: SharedComponent

    // MARK: Initialization

    /// Initializes a `ServiceContainer`.
    ///
    init(sharedComponent: SharedComponent) {
        self.sharedComponent = sharedComponent
    }
}
