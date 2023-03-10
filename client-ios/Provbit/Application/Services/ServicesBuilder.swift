import Foundation
import ProvbitShared

// MARK: - ServicesBuilder

/// A protocol for an object that builds all services used by the application.
protocol ServicesBuilder {
    // MARK: Methods

    /// Initializes all services used by the application and returns them in a `ServiceContainer`.
    ///
    /// - Returns: A `ServiceContainer` containing all of the application services.
    func build() -> ServiceContainer
}

// MARK: - DefaultServicesBuilder

/// The default implementation of an object which builds all of the services and repositories used by the application.
class DefaultServicesBuilder: ServicesBuilder {
    // MARK: Properties

    /// The configuration object to use when building services.
    let config: Config

    // MARK: Initialization

    /// Creates a new `DefaultServicesBuilder` object using the provided configuration.
    ///
    /// - parameter config: The configuration to use when building services.
    init(config: Config) {
        self.config = config
    }

    // MARK: Methods

    func build() -> ServiceContainer {
        ServiceContainer(
            sharedComponent:
            SharedComponent.companion.create(
                driverFactory: DriverFactory(),
                authRepository: Auth0AuthenticationRepository(
                    config: DefaultConfig()
                ),
                ImageRecognitionRepository: ImageRecognitionRepositoryIos(),
                context: BluetoothContext()
            )
        )
    }
}

class ImageRecognitionRepositoryIos: ImageRecognitionRepository {
    func analyzeImage(image _: KotlinByteArray, completionHandler _: @escaping ([ImageAnalysis]?, Error?) -> Void) {}
}
