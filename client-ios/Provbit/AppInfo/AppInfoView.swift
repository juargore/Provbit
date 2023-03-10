import ProvbitShared
import SwiftUI

// MARK: - AppInfoView

/// A `View` for displaying information about the application.
struct AppInfoView: View {
    @ObservedObject var viewModel: AnyObservableViewData<AppInfoViewData>

    var body: some View {
        ScrollView {
            VStack(alignment: .center, spacing: 30) {
                Text(viewModel.viewData.versionInfoLabel.localized())
                Button(viewModel.viewData.tapCountButtonLabel.localized()) {
                    viewModel.viewData.testButtonOnClick()
                }.buttonStyle(ProvbitButtonStyle())

                toastView()

                Button(viewModel.viewData.viewListButtonLabel.localized()) {
                    viewModel.viewData.itemsButtonOnClick()
                }.buttonStyle(ProvbitButtonStyle())

                Button(viewModel.viewData.viewSelectorButtonLabel.localized()) {
                    viewModel.viewData.itemSelectorButtonOnClick()
                }.buttonStyle(ProvbitButtonStyle())

                Button(viewModel.viewData.qrListButtonLabel.localized()) {
                    viewModel.viewData.qrResultsButtonOnClick()
                }.buttonStyle(ProvbitButtonStyle())

                Button(viewModel.viewData.viewStyleGuideButtonLabel.localized()) {
                    viewModel.viewData.viewStyleGuideOnClick()
                }.buttonStyle(ProvbitButtonStyle())

                Button(viewModel.viewData.bluetoothButtonLabel.localized()) {
                    viewModel.viewData.bluetoothButtonOnClick()
                }.buttonStyle(ProvbitButtonStyle())
            }
        }
    }

    /// Creates a new toast view based on the `ToastViewData` value in `viewModel.viewData`.
    @ViewBuilder func toastView() -> some View {
        switch ToastViewDataKs(viewModel.viewData.toast) {
        case let .show(show):
            Text(show.text.localized())
        case .hide:
            Text("Dummy text for height").hidden()
        }
    }
}

#if DEBUG

    struct AppInfoView_Previews: PreviewProvider {
        static var previews: some View {
            Group {
                NavigationView {
                    AppInfoView(viewModel: previewAnyObservable(
                        AppInfoViewData(
                            tapCountLabel: PreviewString("30 taps"),
                            tapCountButtonLabel: PreviewString("Tap to count"),
                            toast: ToastViewData.Show(text: PreviewString("30 taps")),
                            testButtonOnClick: {},
                            itemsButtonOnClick: {},
                            itemSelectorButtonOnClick: {},
                            qrListButtonLabel: PreviewString("Scan QR Code"),
                            qrResultsButtonOnClick: {},
                            imageAnalysisButtonOnClick: {},
                            versionInfoLabel: PreviewString("Version 0.1.0"),
                            viewListButtonLabel: PreviewString("Go to List"),
                            viewSelectorButtonLabel: PreviewString("View Selector"),
                            viewStyleGuideButtonLabel: PreviewString("View Style Guide"),
                            imageAnalysisButtonLabel: PreviewString("image analysis"),
                            viewStyleGuideOnClick: {},
                            bluetoothButtonLabel: PreviewString("Bluetooth"),
                            bluetoothButtonOnClick: {}
                        )
                    ))
                }
            }
        }
    }

#endif
