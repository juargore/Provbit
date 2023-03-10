package ai.bright.provbit.demo.presentation

import ai.bright.provbit.architecture.BaseProcessor
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.architecture.asCommonStateFlow
import ai.bright.provbit.architecture.combine
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject

/**
 * View data for the [QRScanProcessor].
 */
sealed class QRScanViewData {

    abstract val onCameraPermissionChange: (CameraPermissionStatus) -> Unit

    /**
     * Camera permission status had not been determined.
     */
    data class Loading(
        override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit
    ) : QRScanViewData()

    /**
     * Show the QR scanner.
     */
    data class Scan(
        override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit,
        val onCodeScanned: (String) -> Unit,
        val onScanError: (String) -> Unit,
    ) : QRScanViewData()

    /**
     * Show an error when a scan error happens.
     */
    data class Error(
        override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit,
        val errorText: StringDesc = "Something went wrong scanning".desc(),
    ) : QRScanViewData()

    /**
     * Show the result of a QR scan.
     */
    data class Result(
        override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit,
        val resultText: StringDesc,
    ) : QRScanViewData()

    /**
     * User has no camera permission.
     */
    sealed class NoPermission : QRScanViewData() {
        /**
         * On iOS, show a message telling the user to go to settings.
         */
        data class IOS(
            override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit,
            val message: StringDesc = "In order to scan QR codes, you need to enable permission in settings.".desc(),
        ) : NoPermission()

        /**
         * On Android, show either a rationale allowing the user to request permisssions from the
         * OS again, or a message telling the user to go to settigns.
         */
        sealed class Android() : NoPermission() {
            data class ShowRationale(
                override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit
            ) : Android()

            data class NeedsSettings(
                override val onCameraPermissionChange: (CameraPermissionStatus) -> Unit,
                val message: StringDesc = "In order to scan QR codes, you need to enable permission in settings.".desc(),
            ) : Android()
        }
    }
}

/**
 * Models the current status of
 */
sealed class CameraPermissionStatus {
    /**
     * The call to show system dialog requesting permission has not been made.
     */
    object Undetermined : CameraPermissionStatus()

    /**
     * Camera permission is granted.
     */
    object Granted : CameraPermissionStatus()

    /**
     * Camera permission was denied. On Android, there is a property denoting if rationale
     * should be shown allowing the app to request access again. On iOS, no such  notion exists.
     */
    sealed class Denied : CameraPermissionStatus() {
        object IOS : Denied()
        data class Android(val shouldShowRationale: Boolean) : Denied()
    }
}

sealed class QRScanEvent

@Inject
class QRScanProcessor : BaseProcessor<QRScanViewData, QRScanEvent>() {

    private val scanResult = MutableStateFlow<QRScanResult>(QRScanResult.NoResult)
    private val cameraPermissions =
        MutableStateFlow<CameraPermissionStatus>(value = CameraPermissionStatus.Undetermined)

    private val onCameraPermissionChange: (CameraPermissionStatus) -> Unit = {
        cameraPermissions.value = it
    }

    override val viewData: CommonStateFlow<QRScanViewData> =
        combine(scanResult, cameraPermissions, ::combineState).asCommonStateFlow()

    private fun combineState(
        result: QRScanResult,
        permissionStatus: CameraPermissionStatus
    ): QRScanViewData {
        return when (permissionStatus) {
            is CameraPermissionStatus.Denied -> {
                when (permissionStatus) {
                    CameraPermissionStatus.Denied.IOS -> {
                        QRScanViewData.NoPermission.IOS(onCameraPermissionChange)
                    }
                    is CameraPermissionStatus.Denied.Android -> {
                        if (permissionStatus.shouldShowRationale) {
                            QRScanViewData.NoPermission.Android.ShowRationale(
                                onCameraPermissionChange
                            )
                        } else {
                            QRScanViewData.NoPermission.Android.NeedsSettings(
                                onCameraPermissionChange
                            )
                        }
                    }
                }
            }
            CameraPermissionStatus.Granted -> {
                when (result) {
                    QRScanResult.NoResult -> {
                        QRScanViewData.Scan(
                            onCameraPermissionChange = onCameraPermissionChange,
                            onScanError = { scanResult.value = QRScanResult.Error(it) },
                            onCodeScanned = { scanResult.value = QRScanResult.Result(it) },
                        )
                    }
                    is QRScanResult.Result -> {
                        QRScanViewData.Result(
                            onCameraPermissionChange = onCameraPermissionChange,
                            resultText = result.data.desc(),
                        )
                    }
                    is QRScanResult.Error -> {
                        QRScanViewData.Error(
                            onCameraPermissionChange = onCameraPermissionChange,
                            errorText = "Something went wrong scanning. ${result.debugMessage}".desc()
                        )
                    }
                }
            }
            CameraPermissionStatus.Undetermined -> {
                QRScanViewData.Loading { cameraPermissions.value = it }
            }
        }
    }

    /**
     * Models QRScan result.
     */
    sealed class QRScanResult {
        object NoResult : QRScanResult()
        data class Error(val debugMessage: String) : QRScanResult()
        data class Result(val data: String) : QRScanResult()
    }
}