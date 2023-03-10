import ProvbitShared
import SwiftUI

// Note: this is all sort of made-up without any design specs

extension Font {
    static func withStyle(_ style: ProvbitTextTypeKs = .plain) -> Font {
        switch style {
        case .button:
            return .system(.subheadline, design: .default).bold()
        case .plain:
            return .body
        case .headline1:
            return .system(.largeTitle, design: .rounded).bold()
        case .headline2:
            return .system(.title, design: .rounded).bold()
        }
    }
}
