import UIKit

// MARK: - AppDelegate

/// The `AppDelegate` used to handle all application events.
class AppDelegate: NSObject, UIApplicationDelegate {
    // MARK: Properties

    /// The root coordinator of the app.
    var appCoordinator: AppCoordinator?

    /// The main window.
    var window: UIWindow?

    // MARK: Methods

    func application(_ _: UIApplication,
                     didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        let window = UIWindow(frame: UIScreen.main.bounds)
        window.backgroundColor = .systemBackground

        let config = DefaultConfig()
        let servicesBuilder = DefaultServicesBuilder(config: config)
        let services = servicesBuilder.build()
        let appCoordinator = AppCoordinator(services: services)

        self.appCoordinator = appCoordinator

        window.rootViewController = appCoordinator.navigationController
        window.makeKeyAndVisible()
        self.window = window

        return true
    }
}
