import ProvbitShared
import SwiftUI

// MARK: - GraphicsColor

extension GraphicsColor {
    // MARK: Properties

    /// A UIKit `UIColor` representation of this color.
    var uiColor: UIColor {
        UIColor(
            displayP3Red: CGFloat(red) / 255,
            green: CGFloat(green) / 255,
            blue: CGFloat(blue) / 255,
            alpha: CGFloat(alpha) / 155
        )
    }
}
