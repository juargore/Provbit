import ProvbitShared
import SwiftUI

// MARK: - AppCoordinator

/// A `Coordinator` for driving app-wide navigation.
final class AppCoordinator {
    typealias Services = HasSharedComponent

    // MARK: Properties

    private var services: Services

    /// The root view controller where all views are presented.
    var navigationController: UINavigationController

    /// The controller that handles sending app-wide navigation messages.
    var processor: NavProcessor

    /// The coordinator responsible for managing `AppInfoView`s.
    var appInfoCoordinator: AppInfoCoordinator?

    /// The coordinator responsible for managing `LoginView`s.
    var loginCoordinator: LoginCoordinator?

    /// The coordinator responsible for managing `SignInView`s.
    var signInCoordinator: SignInCoordinator?

    /// This top level directly observes the `NavProcessor` view data and events
    /// rather than doing so through some `ViewModel` or `ComponentObserver`.
    /// As such, it needs to be able to unsubscribe from these changes, which requires
    /// cancelling a scope in the `shared` module. These `Closeable` objects
    /// wrap this ability, and we call it on `deinit`. This is similar to the `deinit`
    /// in the afforementioned classes.
    var viewDataCloser: Closeable?
    var eventCloser: Closeable?

    // MARK: Initialization

    /// Creates a new `AppCoordinator`.
    init(services: Services) {
        self.services = services
        processor = services.sharedComponent.navProcessor()
        navigationController = UINavigationController()
        // Subscribe to nav processor events and viewdata
        viewDataCloser = processor.viewData.watch { [weak self] viewData in
            guard let viewData = viewData else { return }
            self?.updateTheme(theme: viewData.theme)
            self?.present(viewData)
        }
    }

    // MARK: Methods

    // MARK: Private Methods

    /// Handles the provided `NavEvent`.
    private func handle(_ event: NavEvent) {
        print("event: \(event)")
    }

    /// Presents the provided `NavViewData`.
    private func present(_ viewData: NavViewData) {
        switch NavStateKs(viewData.state) {
        case .splash:
            showSplash()
        case .unauthenticated:
            showLogin()
//            showSignIn()
        case .authenticated:
            showAppInfoView()
        }
    }

    /// Presents the splash screen.
    private func showSplash() {
        let view = SplashView()
        let viewController = HostingController(rootView: view)
        navigationController.setViewControllers([viewController], animated: false)
    }

    /// Presents the login screen.
    private func showLogin() {
        loginCoordinator = DefaultLoginCoordinator(
            services: services,
            navigationController: navigationController,
            parentCoordinator: self
        )
        loginCoordinator?.showLogin()
    }

    /// Presents the sign in screen.
    private func showSignIn() {
        signInCoordinator = DefaultSignInCoordinator(
            services: services,
            navigationController: navigationController,
            parentCoordinator: self
        )
        signInCoordinator?.showSignIn()
    }

    /// Presents the home screen.
    private func showAppInfoView() {
        loginCoordinator = nil // ensure reference isn't held on to
        appInfoCoordinator = DefaultAppInfoCoordinator(
            services: services,
            navigationController: navigationController,
            parentCoordinator: self
        )
        appInfoCoordinator?.showAppInfoView()
    }

    deinit {
        viewDataCloser?.close()
        eventCloser?.close()
    }

    /// Update the shared theme for the project.
    /// Note: for now this is getting called whenever any state changes. (Hopefully that's not too frequent.)
    private func updateTheme(theme: ProvbitTheme) {
        ProvbitTheme.current = theme

        let coloredAppearance = UINavigationBarAppearance()
        coloredAppearance.configureWithTransparentBackground()
        coloredAppearance.backgroundColor = theme.primaryColor.uiColor
        coloredAppearance.titleTextAttributes = [.foregroundColor: theme.buttonPrimaryTextColor.uiColor]
        coloredAppearance.largeTitleTextAttributes = [.foregroundColor: theme.buttonPrimaryTextColor.uiColor]

        UINavigationBar.appearance().standardAppearance = coloredAppearance
        UINavigationBar.appearance().compactAppearance = coloredAppearance
        UINavigationBar.appearance().scrollEdgeAppearance = coloredAppearance

        navigationController.navigationBar.setNeedsLayout()
    }
}
