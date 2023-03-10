import Auth0
import ProvbitShared

// MARK: - Auth0AuthenticationRepository

/// An `AuthenticationRepository` that integrates with Auth0.
final class Auth0AuthenticationRepository: AuthenticationRepository {
    // MARK: - Properties

    /// The credentials manager for this repository. An `Auth0` backed credential storage option.
    let credentialsManager: CredentialsManager

    /// The configuration for this repository. Contains important `Auth0` configuration information,
    /// such as the client id and domain.
    let config: Config

    // MARK: Initialization

    /// Creates a new `Auth0AuthenticationRepository` with the provided `Config`.
    ///
    /// - parameter config: The configuration object to use.
    init(config: Config) {
        let authentication = Auth0.authentication(clientId: config.auth0ClientID, domain: config.auth0Domain)
        credentialsManager = CredentialsManager(authentication: authentication)
        self.config = config
    }

    // MARK: Methods

    func getCredentials(completionHandler completion: @escaping (AuthenticationRepositoryCredentials?, Error?)
        -> Void) {
        credentialsManager.credentials { error, credentials in
            completion(credentials?.authCredentials, error)
        }
    }

    func isAuthenticated(completionHandler completion: @escaping (KotlinBoolean?, Error?) -> Void) {
        let hasValid = credentialsManager.hasValid()
        completion(KotlinBoolean(bool: hasValid), nil)
    }

    func login(connection _: AuthenticationRepositoryAuthenticationConnection,
               isSignUp: Bool,
               completionHandler completion: @escaping (AuthenticationRepositoryCredentials?, Error?) -> Void) {
        Auth0
            .webAuth(clientId: config.auth0ClientID, domain: config.auth0Domain)
            .scope("openid profile email read:hardware")
            .parameters(["action": isSignUp ? "signup" : "sign"])
            .start { result in
                switch result {
                case let .success(credentials):
                    print(credentials)
                    _ = self.credentialsManager.store(credentials: credentials)
                    completion(credentials.authCredentials, nil)
                case let .failure(error):
                    print(error)
                    completion(nil, error)
                }
            }
    }

    func logout(completionHandler completion: @escaping (KotlinUnit?, Error?) -> Void) {
        credentialsManager.revoke { error in
            // We have to pass in a `KotlinUnit` if no error was encountered.
            let unit = error == nil ? KotlinUnit() : nil
            completion(unit, error)
        }
    }
}

// MARK: - Credentials

extension Credentials {
    // MARK: Properties

    /// Creates an `AuthenticationRepositoryCredentials` representation of this `Credentials` object.
    var authCredentials: AuthenticationRepositoryCredentials? {
        guard let idToken = idToken,
              let accessToken = accessToken,
              let expiresIn = expiresIn
        else { return nil }

        return AuthenticationRepositoryCredentials(
            idToken: idToken,
            accessToken: accessToken,
            expiration: Timestamp(epochSeconds: Int64(expiresIn.timeIntervalSince1970))
        )
    }
}
