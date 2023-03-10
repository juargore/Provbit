import ProvbitShared
import SwiftUI

struct ButtonView: View {
    let state: ButtonViewData

    var body: some View {
        Button {
            state.onClick()
        } label: {
            Text(state.content.localized())
        }.buttonStyle(ProvbitButtonStyle(style: state.styleKs))
    }
}

extension ButtonViewData {
    var styleKs: ProvbitButtonTypeKs {
        ProvbitButtonTypeKs(style)
    }
}
