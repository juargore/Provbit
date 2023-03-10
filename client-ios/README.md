# Bright Provbit - iOS Client

## Contents

- [Installation](#installation)
- [Testing](#testing)

## Installation

1. Clone the repository.

1. Install dependencies.

    - `mint`
    - `java`

        **Note**: This section needs flushing out. Because there is a build phase that runs the `gradelw` command in the parent project, it is possible that the entire Android toolchain is also a dependency.
   
1. Decrypt Secrets file

```sh
openssl enc -d -md md5 -aes-256-cbc -base64 -in buildSrc/src/main/java/Secrets.kt.enc -out buildSrc/src/main/java/Secrets.kt
```

1. Change directories into the `client-ios/` folder in the project.

1. Bootstrap the project:

    ```sh
    $ ./Scripts/bootstrap.sh
    // Secrets will be in the decoded file noted above
    ```

1. Run the `Provbit` target in Xcode.

## To Update Build Secrets

```sh
openssl enc -md md5 -aes-256-cbc -base64 -in buildSrc/src/main/java/Secrets.kt -out buildSrc/src/main/java/Secrets.kt.enc
```

## Testing

This project uses SwiftUI, and the following additional dependency for tests is used:

1. [ViewInspector](https://github.com/nalexn/ViewInspector)

    - This allows views to be marked with the `Inspectable` protocol, and then tested in a variety of ways.

