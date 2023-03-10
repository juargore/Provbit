# Bright Provbit

> "provbit" - From Swedish 'example' or 'sample'.

Provbit is the Bright mobile demo application.
This project is used to demonstrate and prove out architecture setups as well as demo new functionality.

Provbit is a Kotlin Multiplatform project containing three core components.
In general this mirrors the overall structure of future Bright Mobile applications.
``
1. [client-ios](client-ios) - iOS Application
1. [client-android](client-android)  -  Android Application
1. [shared](shared) - [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/) code

## Setup

### Shared Setup

The following steps are required for any development on this project. KMM, Android, and iOS development all require these steps. Separate sections are listed below for the specific platforms.

1. Clone the repository:

    ```sh
    $ git clone https://github.com/BrightDotAi/bright-provbit.git
    ```

1. Install [Mint](https://github.com/yonaskolb/mint):

    ```sh
    $ brew install mint
    ```

    When using Mint on an M1 machine that has been installed via homebrew, set up a [symlink](https://en.wikipedia.org/wiki/Symbolic_link) so that `mint` commands can be run within Xcode.

    ```sh
    $ sudo ln -s /opt/homebrew/bin/mint /usr/local/bin/mint
    ```

    Or alternatively, without `brew`, clone the Mint repo into a temporary directory.

    ```sh
    $ git clone https://github.com/yonaskolb/Mint.git
    $ cd Mint
    $ make
    ```

1. Install the Java JDK:

    Install whichever Java JDK you would like to use, but make sure that it supports Java 11.

    ```sh
    brew install openjdk
    ```

    If using homebrew to install OpenJDK, you will need to add the following to your `.zshrc`:

    ```sh
    export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"
    export CPPFLAGS="-I/opt/homebrew/opt/openjdk/include"
    ```

    Additionally, add the following symlink from `Library/Java/JavaVirtualMachines` to your OpenJDK installation. This example is for versions of OpenJDK installed via homebrew.

    ```sh
    sudo ln -s /opt/homebrew/opt/openjdk/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk.jdk
    ```

    This command is run with `sudo` because you need administrator permissions to edit files in the system `Library` folder.

1. Create `local.properties`:

    `local.properties` is a file that contains local configuration options for the project. Currently, your GitHub username and an access token with `read:packages` access is required for gradle builds.

    ```sh
    github.user="your_username"
    github.token="your_token"
    ```

1. Decode the `secrets.properties`:

    ```sh
    openssl enc -d -md md5 -aes-256-cbc -base64 -in buildSrc/src/main/java/Secrets.kt.enc -out buildSrc/src/main/java/Secrets.kt
    ```

    The decryption password can be obtained by someone working on the project.

### iOS

To complete the setup of the iOS project, navigate to the `client-ios` folder and bootstrap the project.

```sh
$ cd client-ios
$ Scripts/bootstrap.sh
```

## View Hierarchy

The fundamental component of the Provbit View Hierarchy is the `StateFlow` (wrapped here as a
`CommonStateFlow` to allow for interoperability with iOS). Each `StateFlow` represents a point at
which redrawing of the view should occur when data is updated. Ultimately, the entirety of the view
comprises `StateFlow` and `data class` objects. Each `StateFlow` is consumed by native view libraries
as either a `consumeAsState()` in Jetpack Compose or as an `@ObservedObject` in SwiftUI. By correctly
structuring these objects, we can avoid excessive redraws and unnecessary rebinding.

The highest level view object is a `Processor`. In general, a `Processor` will correspond to a single
screen, and is analogous to a `ViewModel` (named differently here to avoid collision with AndroidX
library naming conventions). Each `Processor` contains a `StateFlow` of view data and a `Flow` of
events. This `events` `Flow` is used to signal navigational movements to the native platform. As
described above, the `viewData` `StateFlow` should will be observed and drawn by the native UI
framework.

By convention, a "component" is a `StateFlow` defined around a specific type of view data. However,
there is currently no "component" interface or class type, as all needed behavior is found in `StateFlow`.
For example the `editTextComponent` function returns a `CommonStateFlow<EditTextViewData` object.
This "component" manages its internal state and modification.

If all of the underlying view components are themselves `StateFlows`, it may not be necessary to
propagate updates for a a `Processor` past initialization. For example, in the `FormProcessor`, we
can use the `CommonStateFlow.singleton` constructor to emit a *single* `FormViewData` object. Because
the changes to the text fields flow through child `StateFlows`, the overarching `ViewData` never
needs to redraw itself.

## Contributing

Please Review [Code Review and Git Workflow for Mobile Repositories (iOS and Android)](https://wiki.brightai.dev/en/Engineering/Client-Platform/code-review)

### Commit Guidelines

Please Review [Conventional Comits](https://wiki.brightai.dev/en/Engineering/Client-Platform/Conventional-Commits)

### Adding a new screen

Steps for adding a new screen to Provbit.

1. Identify a naming prefix for the new screen.

    - Further steps will include examples like `-Name`, which assumes you substitute the `-` with this prefix. For example, given a prefix of "TheBestExample", `-ViewData` becomes `TheBestExampleViewData`.

1. Identify user-facing strings on the new screen.

    - Each string should then be given an identifier (unique to the entire application), and added to `shared/src/commonMain/resources/MR/base/strings.xml` (And also to any additional translation `strings.xml` files.)

1. Add resources to the shared project. This includes any images.

1. Add a new `BaseProcessor` subclass in the shared project.

    - `-Processor` files exist in the `shared/src/commonMain/kotlin/ai/bright/provbit/demo/presentation/` folder.
    - These files also contain `-ViewData` and `-Event` class definitions.
        - `ViewData` should contain a reference to any user-facing strings as `StringDesc` properties.
        - `ViewData` should also include any "actions" emited by the new screen in the form of lamdas. An example action would be a button pressed on the screen.
        - `Events` are commonly defined as a sealed class and represent tasks the view will need to be able to complete. An example event would be navigation from the screen in question to another screen in the application.
    - You'll also need to add a line to the `SharedComponent.kt` file.

1. Add the new Android `-Composable` screen defintion.

    - These files exist in the `client-android/src/main/java/ai/bright/provbit/android/screens/` directory. This should include strings and images identified in previous steps.

1. Add new iOS files, modify some existing ones.

    - Modify the `ProcessorSwift.swift` file to add a new conformance extension for your previously created `-Processor` class.
    - New files should most likely be created in the `client-ios/Provbit/` directory, (typically in an appropriately named subfolder):
        - A `-View.swift` file. Build the view here, using the strings and images identified previously.
        - An `-EventHandler.swift` file if one is necessary.
        - A `-Coordinator.swift` file. The new coordinator may also be optional, but you will need to add code somewhere that handles the instantiation of the `-View` object, wrapping it in a `HostingViewController` instance, and adding that controller to the navigation stack. If a new coordinator is not added, you'll want to modify one that is the closest in terms of navigation stack to where the new screen will appear.

1. Create a navigation "Event" wherever you want to trigger displaying the new screen.

    - This should be a new "case" on the `-Event` definition of whatever existing screen will be displaying this new screen.
    - There will be additional code necessary on both platforms to then display the new screen:
        - For Android, the event is handed in the `MainNavComposable.kt` file. Find the screen defintion for the one with the new event, and handle it appropriately.
        - For iOS: use the `-EventHandler` for the screen in question. There are a few things that might happen as a result, the most likely of which is creating a new instance of the `-Coordinator` (created in the previous step) as a property on the existing screen's coordinator. At the very least, the new coordinator should have a function called that creates the new `-View`.


