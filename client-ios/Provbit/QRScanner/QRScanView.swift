import AVKit
import CodeScanner
import ProvbitShared
import SwiftUI

/// A `View` for showing the QR scanning flow.
struct QRScanView: View {
    @ObservedObject var component: AnyObservableViewData<QRScanViewData>

    /// A property used to class the static
    let aVCaptureDevice: AVCaptureDeviceProvbit.Type

    /// Initializes a `QRScanView`.
    /// - Parameters:
    ///   - viewModel: The view model used by this view.
    ///   - aVCaptureDevice: The `aVCaptureDevice` type.
    init(component: AnyObservableViewData<QRScanViewData>,
         aVCaptureDevice: AVCaptureDeviceProvbit.Type = AVCaptureDevice.self) {
        self.component = component
        self.aVCaptureDevice = aVCaptureDevice
    }

    var body: some View {
        let viewData = QRScanViewDataKs(component.viewData)
        VStack(alignment: .center, spacing: 30) {
            switch viewData {
            case .loading:
                Text("LOADING")
            case let .scan(scan):
                CodeScannerView(codeTypes: [.qr], showViewfinder: true) { response in
                    switch response {
                    case let .success(result):
                        scan.onCodeScanned(result.string)
                    case .failure:
                        scan.onCodeScanned("Unknown error.")
                    }
                }
            case let .error(error):
                Text(error.errorText.localized())
            case let .result(result):
                Text(result.resultText.localized())
            case let .noPermission(noPermission):
                switch QRScanViewDataNoPermissionKs(noPermission) {
                case let .iOS(iOSNoPermission):
                    Text(iOSNoPermission.message.localized())
                case .android:
                    FailableView()
                }
            }
        }.onAppear(perform: {
            aVCaptureDevice.requestAccess(for: .video) { granted in
                DispatchQueue.main.async {
                    if granted {
                        component.viewData.onCameraPermissionChange(CameraPermissionStatus.Granted())
                    } else {
                        component.viewData.onCameraPermissionChange(CameraPermissionStatus.DeniedIOS())
                    }
                }
            }
        })
    }
}
