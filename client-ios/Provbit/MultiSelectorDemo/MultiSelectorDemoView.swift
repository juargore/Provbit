import ProvbitShared
import SwiftUI

// MARK: - ItemListView

/// A `View` for displaying a list of items.
struct MultiSelectorDemoView: View {
    @ObservedObject var viewModel: AnyObservableViewData<MultiSelectorDemoViewData>

    var body: some View {
        VStack(spacing: 0) {
            Text(viewModel.viewData.title)
            Text(viewModel.viewData.dataState)
            MultiSelectorView(component: viewModel.viewData.multiSelector)
        }
    }
}
