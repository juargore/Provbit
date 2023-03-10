import SwiftUI

// MARK: - SearchBar

/// A `UISearchBar` wrapper for iOS 14.
///
/// Adapted from: https://www.albertomoral.com/blog/uisearchbar-and-swiftui
struct SearchBar: UIViewRepresentable {
    // MARK: Properties

    /// How the clear button should be displayed. Defaults to `.never`.
    var clearButtonMode: UITextField.ViewMode = .never

    /// The placeholder displayed in the search bar.
    let placeholder: String

    /// A flag indicating if the cancel button should be displayed or not.
    let showsCancelButton = true

    /// A private flag indicating if the cancel button should currently be displayed or not.
    @State private var shouldShowCancelButton = false

    /// The text entered by the user.
    @Binding var text: String

    // MARK: Methods

    func makeCoordinator() -> SearchBar.Coordinator {
        Coordinator(
            text: $text,
            shouldShowCancelButton: $shouldShowCancelButton,
            showsCancelButton: showsCancelButton
        )
    }

    func makeUIView(context: UIViewRepresentableContext<SearchBar>) -> UISearchBar {
        let searchBar = UISearchBar(frame: .zero)
        searchBar.delegate = context.coordinator
        searchBar.autocapitalizationType = .none
        searchBar.placeholder = placeholder
        searchBar.showsCancelButton = shouldShowCancelButton
        searchBar.searchTextField.clearButtonMode = .always
        searchBar.searchBarStyle = .minimal
        return searchBar
    }

    func updateUIView(_ uiView: UISearchBar, context _: UIViewRepresentableContext<SearchBar>) {
        uiView.text = text
        uiView.setShowsCancelButton(shouldShowCancelButton, animated: true)
    }
}

// MARK: - SearchBar.Coordinator

extension SearchBar {
    class Coordinator: NSObject, UISearchBarDelegate {
        @Binding var text: String

        @Binding var shouldShowCancelButton: Bool

        let showsCancelButton: Bool

        init(text: Binding<String>, shouldShowCancelButton: Binding<Bool>, showsCancelButton: Bool) {
            _text = text
            _shouldShowCancelButton = shouldShowCancelButton
            self.showsCancelButton = showsCancelButton
        }

        func searchBar(_: UISearchBar, textDidChange searchText: String) {
            text = searchText
        }

        func searchBarSearchButtonClicked(_: UISearchBar) {
            UIApplication.shared.endEditing()
        }

        func searchBarTextDidBeginEditing(_: UISearchBar) {
            shouldShowCancelButton = showsCancelButton
        }

        func searchBarTextDidEndEditing(_: UISearchBar) {
            shouldShowCancelButton = text.isEmpty ? false : showsCancelButton
        }

        func searchBarCancelButtonClicked(_: UISearchBar) {
            text = ""
            UIApplication.shared.endEditing()
        }
    }
}
