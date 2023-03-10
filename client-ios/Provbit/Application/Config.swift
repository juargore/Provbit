import Foundation

// MARK: - Config

/// A protocol representing configuration objects that can be used to refer to configuration values
/// needed for uses such as API authorization.
protocol Config {
    // MARK: Properties

    /// The callback scheme used when authorizing with Auth0.
    var auth0CallbackScheme: String { get }

    /// The client id needed to authorize this app with Auth0 for login purposes.
    var auth0ClientID: String { get }

    /// The domain for this app used when authorizing with Auth0.
    var auth0Domain: String { get }
}

// MARK: - DefaultConfig

/// The default configuration object.
struct DefaultConfig: Config {
    // MARK: Properties

    let auth0CallbackScheme = "brightai"
    let auth0ClientID = ProvbitKeys.AuthClientID
    let auth0Domain = "brightai-core-dev.us.auth0.com"
}

// MARK: - MockConfig

/// A mock configuration object used in testing and previews.
struct MockConfig: Config {
    // MARK: Properties

    let auth0CallbackScheme = "callback"
    let auth0ClientID = "client id"
    let auth0Domain = "domain"
}
