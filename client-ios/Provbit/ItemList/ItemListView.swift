import ProvbitShared
import SwiftUI

extension Array {
    /// Returns an optional `Element` by passing in an `IndexSet`.
    /// This currently does not function properly for sectioned lists.
    func objectAt(_ indexSet: IndexSet) -> Element? {
        let indexPath = IndexPath(indexes: indexSet)
        guard let index = indexPath.first else { return nil }
        let item = self[index]
        return item
    }
}

// MARK: - ItemListView

/// A `View` for displaying a list of items.
struct ItemListView: View {
    @ObservedObject var viewModel: AnyObservableViewData<ItemListViewData>

    var body: some View {
        VStack(spacing: 0) {
            EditTextView(component: observe(viewModel.viewData.searchText))
            ListDisplayView(
                parentViewModel: viewModel,
                component: observe(viewModel.viewData.itemList)
            )
        }
    }
}

struct ListDisplayView: View {
    @ObservedObject var parentViewModel: AnyObservableViewData<ItemListViewData>
    @ObservedObject var component: AnyObservableViewData<ItemListContent>

    /// Holds the state of the `List`.
    @State var isEditMode: EditMode = .inactive

    var body: some View {
        List {
            ForEach(component.viewData.items, id: \.self.item.guid) { item in
                VStack(alignment: .leading) {
                    Text(item.name)
                        .font(.body)
                        .fontWeight(.bold)
                    Text(item.desc)
                        .font(.caption)
                }.onTapGesture {
                    item.onClick()
                }
            }
            .onDelete { indexSet in
                let itemToDelete = component.viewData.items.objectAt(indexSet)
                parentViewModel.viewData.onDelete(itemToDelete!.item)
            }
            .onMove { indexSet, newOffset in
                component.viewData.items.move(fromOffsets: indexSet, toOffset: newOffset)
            }
        }
        .animation(.default)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                EditButton()
            }
            ToolbarItem(placement: .navigationBarTrailing) {
                Button {
                    parentViewModel.viewData.onAdd()
                } label: {
                    Image(systemName: "plus")
                }
            }
        }.environment(\.editMode, $isEditMode)
    }
}

extension EditTextViewData {
    func binding() -> Binding<String> {
        Binding<String>(
            get: { self.text },
            set: { self.onTextChanged($0) }
        )
    }
}

extension Binding {
    subscript<Subject>(
        dynamicMember keyPath: WritableKeyPath<Value, Subject>
    ) -> Binding<Subject> {
        Binding<Subject>(
            get: { self.wrappedValue[keyPath: keyPath] },
            set: { self.wrappedValue[keyPath: keyPath] = $0 }
        )
    }
}
