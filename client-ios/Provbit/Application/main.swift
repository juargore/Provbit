import UIKit

/// Replace the `AppDelegate` with a `TestingAppDelegate` while running unit tests.
/// This avoids any unnecessary networking or initial configuration not required by the unit tests.
let appDelegateClass: AnyClass? =
    NSClassFromString("ProvbitTests.TestingAppDelegate") ?? AppDelegate.self

UIApplicationMain(CommandLine.argc, CommandLine.unsafeArgv, nil, NSStringFromClass(appDelegateClass!))
