import ProvbitShared
import SwiftUI

// MARK: - ResourcesColorResource.Themed

extension ResourcesColorResource.Themed {
    // MARK: Properties

    /// A SwiftUI `Color` representation of this color.
    var color: Color { Color(uiColor) }

    /// A UIKit `UIColor` representation of this color.
    var uiColor: UIColor {
        UIColor { traitCollection in
            switch traitCollection.userInterfaceStyle {
            case .light, .unspecified:
                return self.light.uiColor
            case .dark:
                return self.dark.uiColor
            @unknown default:
                return self.light.uiColor
            }
        }
    }
}
