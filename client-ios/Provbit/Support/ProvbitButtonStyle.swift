import ProvbitShared
import SwiftUI

struct ProvbitButtonStyle: ButtonStyle {
    var style: ProvbitButtonTypeKs = .primary

    func makeBody(configuration: Configuration) -> some View {
        HStack {
            configuration.label
                .font(font())
                .foregroundColor(labelColor())
                .padding(20)
        }
        .background(backgroundColor())
        .border(borderColor(), width: 1)
        .scaleEffect(configuration.isPressed ? 0.95 : 1)
    }

    private func backgroundColor() -> Color {
        switch style {
        case .primary:
            return ProvbitTheme.primaryColor()
        case .secondary:
            return ProvbitTheme.secondaryColor()
        case .tertiary:
            return .clear
        }
    }

    private func borderColor() -> Color {
        switch style {
        case .primary, .secondary:
            return ProvbitTheme.primaryColor()
        case .tertiary:
            return .clear
        }
    }

    private func font() -> Font {
        Font.withStyle(.button)
    }

    private func labelColor() -> Color {
        switch style {
        case .primary:
            return ProvbitTheme.buttonPrimaryText()
        case .secondary:
            return ProvbitTheme.buttonSecondaryText()
        case .tertiary:
            return ProvbitTheme.primaryColor()
        }
    }
}
