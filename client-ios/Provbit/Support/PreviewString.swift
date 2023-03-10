#if DEBUG

    import Foundation
    import ProvbitShared

    // MARK: - PreviewString

    /// A previewable representation of a string. This is used in place of `moko-resource`'s generated strings
    /// so that we can easily create `View` previews.
    class PreviewString: ResourcesStringDesc {
        // MARK: Properties

        /// The value of this string resource. This should be the value you want to see in the preview.
        private var value: String

        // MARK: Initialization

        /// Creates a new `PreviewString` using the value provided.
        ///
        /// - parameter value: The value to be displayed in the preview.
        init(_ value: String) {
            self.value = value
        }

        // MARK: Methods

        func localized() -> String {
            value
        }
    }

#endif
