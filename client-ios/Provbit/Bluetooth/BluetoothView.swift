import ProvbitShared
import SwiftUI

// extension DeviceListRowViewData: Identifiable {}
struct BluetoothView: View {
    @ObservedObject var component: AnyObservableViewData<BluetoothScanViewData>

    var body: some View {
        bluetoothScanScreen(BluetoothScanViewDataKs(component.viewData))
            .animation(.default, value: component.viewData)
    }

    @ViewBuilder
    func bluetoothScanScreen(_ viewData: BluetoothScanViewDataKs) -> some View {
        switch viewData {
        case let .preScan(viewData): preScan(viewData)
        case .loading: loading()
        case let .scanError(viewData): scanError(viewData)
        case let .deviceList(viewData): deviceList(viewData)
        }
    }

    @ViewBuilder
    func loading() -> some View {
        ProgressView()
    }

    @ViewBuilder
    func preScan(_ viewData: BluetoothScanViewData.PreScan) -> some View {
        ButtonView(state: viewData.startScanButton)
    }

    @ViewBuilder
    func scanError(_ viewData: BluetoothScanViewData.ScanError) -> some View {
        VStack {
            Text(viewData.errorText.localized())
            ButtonView(state: viewData.retryButton)
        }
    }

    @ViewBuilder
    func deviceList(_ viewData: BluetoothScanViewData.DeviceList) -> some View {
        List {
            Section {
                ForEach(viewData.connectedItems, id: \.id) { row in
                    deviceRow(row)
                }
            } header: {
                Text(viewData.connectedHeader.localized())
            }
            Section {
                ForEach(viewData.disconnectedItems, id: \.id) { row in
                    deviceRow(row)
                }
            } header: {
                Text(viewData.notConnectedHeader.localized())
            }
        }
    }

    @ViewBuilder
    func deviceRow(_ viewData: BluetoothDeviceRowViewData) -> some View {
        let status = BluetoothDeviceRowViewDataConnectionStateViewDataKs(viewData.connectionState)
        VStack(alignment: .leading) {
            Text(viewData.name.localized())
                .font(.title3)
            Text(viewData.id)
                .font(.caption)
            switch status {
            case let .connected(viewData): ButtonView(state: viewData.button)
            case let .notConnected(viewData): ButtonView(state: viewData.button)
            case .connecting: ProgressView()
            }
        }
    }
}

#if DEBUG

#endif
