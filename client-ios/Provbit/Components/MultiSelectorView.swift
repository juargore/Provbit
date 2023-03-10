import ProvbitShared
import SwiftUI

struct MultiSelectorView: View {
    var component: TextMultiSelectorViewData

    @ObservedObject var selectedItems: AnyObservableViewData<TextMultiSelectorViewData.SelectorContentList>
    @ObservedObject var selectableItems: AnyObservableViewData<TextMultiSelectorViewData.SelectorContentList>

    init(component: TextMultiSelectorViewData) {
        self.component = component
        selectedItems = observe(component.selectedItems)
        selectableItems = observe(component.selectableItems)
    }

    var body: some View {
        print("Reveal Multi-selector")
        return VStack(spacing: 0) {
            List {
                ForEach(selectedItems.viewData.items, id: \.self.content) { item in
                    VStack(alignment: .leading) {
                        Text(item.content)
                            .font(.body)
                            .fontWeight(.bold)
                    }.onTapGesture {
                        item.onSelected()
                    }
                }
            }
            .animation(.default)
            Text("Filter list")
            EditTextView(component: observe(component.filter))
            List {
                ForEach(selectableItems.viewData.items, id: \.self.content) { item in
                    VStack(alignment: .leading) {
                        Text(item.content)
                            .font(.body)
                            .fontWeight(.bold)
                    }.onTapGesture {
                        item.onSelected()
                    }
                }
            }
        }
    }
}
