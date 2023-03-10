import ProvbitShared
import SwiftUI
import UIKit

extension ProvbitTheme {
    static var current: ProvbitTheme?

    static func buttonPrimaryText() -> Color {
        current?.buttonPrimaryTextColor.color ?? Color.white
    }

    static func buttonSecondaryText() -> Color {
        current?.buttonSecondaryTextColor.color ?? Color.blue
    }

    static func primaryColor() -> Color {
        current?.primaryColor.color ?? Color.blue
    }

    static func secondaryColor() -> Color {
        current?.secondaryColor.color ?? Color.white
    }
}
