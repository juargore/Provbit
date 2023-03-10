import ProvbitShared
import SwiftUI

// MARK: - ItemListView

/// A `View` for displaying details of an `Item`.
struct ItemListDetailView: View {
    @ObservedObject var viewModel: AnyObservableViewData<ItemDetailViewData>

    var body: some View {
        Group {
            switch ItemDetailViewDataKs(viewModel.viewData) {
            case let .showItem(itemViewData):
                EditTextView(component: observe(itemViewData.name))
                EditTextView(component: observe(itemViewData.desc))
            case .error:
                Text("Failed To Load")
            case .loading:
                Text("Loading...")
            }
        }.toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                ButtonView(state: saveButtonViewData)
            }
        }
    }

    var saveButtonViewData: ButtonViewData {
        let showItem = viewModel.viewData as? ItemDetailViewData.ShowItem
        return showItem?.save ?? ButtonViewData(
            content: PreviewStringDesc("TEST"),
            style: ProvbitButtonType.Primary(),
            onClick: {}
        )
    }
}
