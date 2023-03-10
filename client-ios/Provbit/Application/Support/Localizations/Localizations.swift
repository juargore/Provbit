import Foundation

/// Localizations used throughout the application.
///
enum Localizations {
    /// Localizations for general phrases.
    ///
    enum General {
        static let cancel = NSLocalizedString(
            "General.Cancel",
            value: "Cancel",
            comment: "The general term for cancelling a task."
        )

        static let done = NSLocalizedString(
            "General.Done",
            value: "Done",
            comment: "The general term for finishing a task."
        )

        static let ok = NSLocalizedString( // swiftlint:disable:this identifier_name
            "General.OK",
            value: "OK",
            comment: "The title of the OK button displayed throughout the app."
        )
    }
}
