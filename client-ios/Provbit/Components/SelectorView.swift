import ProvbitShared
import SwiftUI

struct SelectorView: View {
    let state: SelectorViewData

    var body: some View {
        Picker(
            "",
            selection: Binding<Int>(
                get: {
                    Int(state.selectionIdx)
                }, set: {
                    state.select(KotlinInt(integerLiteral: $0))
                }
            )
        ) {
            ForEach(Array(zip(
                state.allElements.indices,
                state.allElements
            )), id: \.0) { _, item in
                Text(item)
            }
        }
    }
}
