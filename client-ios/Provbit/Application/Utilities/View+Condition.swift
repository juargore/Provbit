import SwiftUI

extension View {
    /// Applies the given transform if the given condition evaluates to `true`.
    ///
    /// Source: https://www.avanderlee.com/swiftui/conditional-view-modifier/
    ///
    /// - Parameters:
    ///   - condition: The condition to evaluate.
    ///   - transform: The transform to apply to the source `View`.
    /// - Returns: Either the original `View` or the modified `View` if the condition is `true`.
    ///
    @ViewBuilder func `if`<Content: View>(
        _ condition: @autoclosure () -> Bool,
        transform: (Self) -> Content
    ) -> some View {
        if condition() {
            transform(self)
        } else {
            self
        }
    }

    /// Applies the given transform if the value is not optional.
    ///
    /// - Parameters:
    ///   - value: The value to check if it's not optional.
    ///   - transform: The transform the apply to the source `View`.
    /// - Returns: Either the original `View` or the modified `View` if the value is not optional.
    ///
    @ViewBuilder func ifLet<Value, Content: View>(_ value: Value?, transform: (Self, Value) -> Content) -> some View {
        if let value = value {
            transform(self, value)
        } else {
            self
        }
    }
}
