package ai.bright.provbit.demo.presentation

import ai.bright.provbit.AppConstants
import ai.bright.provbit.MR
import ai.bright.provbit.architecture.BaseProcessor
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.architecture.asCommonStateFlow
import ai.bright.provbit.architecture.combine
import ai.bright.provbit.util.ProvbitLogger
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

/**
 * Models info to be displayed on the app info screen.
 */
data class AppInfoViewData(
    val tapCountLabel: StringDesc,
    val tapCountButtonLabel: StringDesc = MR.strings.tap_to_count.desc(),
    val toast: ToastViewData,
    val testButtonOnClick: () -> Unit,
    val itemsButtonOnClick: () -> Unit,
    val itemSelectorButtonOnClick: () -> Unit,
    val qrListButtonLabel: StringDesc = MR.strings.qr_show_scanner.desc(),
    val qrResultsButtonOnClick: () -> Unit,
    val imageAnalysisButtonOnClick: () -> Unit,
    val versionInfoLabel: StringDesc = MR.strings.version_name_format.format(AppConstants.versionName),
    val viewListButtonLabel: StringDesc = MR.strings.view_list.desc(),
    val viewSelectorButtonLabel: StringDesc = MR.strings.view_selector.desc(),
    val viewStyleGuideButtonLabel: StringDesc = MR.strings.view_style_guide.desc(),
    val imageAnalysisButtonLabel: StringDesc = MR.strings.image_analysis_title.desc(),
    val viewStyleGuideOnClick: () -> Unit,
    val bluetoothButtonLabel: StringDesc = MR.strings.bluetooth_show_screen_button_title.desc(),
    val bluetoothButtonOnClick: () -> Unit,
)

sealed class ToastViewData {
    object Hide : ToastViewData()
    data class Show(
        val text: StringDesc,
    ) : ToastViewData()
}

sealed class AppInfoEvent {
    object ToBluetooth: AppInfoEvent()
    object ToList: AppInfoEvent()
    object ToQRResults: AppInfoEvent()
    object ToSelector: AppInfoEvent()
    object ToStyleGuide: AppInfoEvent()
    object ToImageAnalysis: AppInfoEvent()
}

@Inject
class AppInfoProcessor : BaseProcessor<AppInfoViewData, AppInfoEvent>() {

    /**
     * Keep track of job that shows/hides the toast so that it can be cancelled.
     */
    private var showToastJob: Job = Job().apply { complete() }

    /**
     * Keep track of number of times the user has tapped the button.
     */
    private val tapCount = MutableStateFlow(0)

    /**
     * Keep track of whether or not the toast should be showing.
     */
    private val isToastShowing = MutableStateFlow(false)

    private val log = ProvbitLogger(tag = "AppInfoProcessor", persistLogs = true)

    override val viewData: CommonStateFlow<AppInfoViewData> =
        combine(tapCount, isToastShowing, ::combineState)
            .asCommonStateFlow()

    /**
     * User clicked the test button.
     */
    private fun handleTestButtonClick() {
        // increment the tap count:
        tapCount.value = tapCount.value + 1
        if(tapCount.value >= 20) log.e("Test button has been clicked \"too many\" (${tapCount.value}) times and is simulating an error", Throwable())
        else log.v("Test button has been clicked ${tapCount.value} times")

        // start job to show toast for 3 seconds:
        showToastJob.cancel()
        showToastJob = launch {
            isToastShowing.value = true
            delay(3000)
            isToastShowing.value = false
        }
    }

    /**
     * Combine internal state into an [AppInfoViewData] for display in UI.
     */
    private fun combineState(tapCount: Int, isToastShowing: Boolean): AppInfoViewData {
        val toastViewData = if (isToastShowing) {
            ToastViewData.Show(MR.strings.tap_count_message_format.format(tapCount))
        } else {
            ToastViewData.Hide
        }
        return AppInfoViewData(
            tapCountLabel = MR.plurals.tap_count_plural.format(tapCount, tapCount),
            toast = toastViewData,
            testButtonOnClick = this::handleTestButtonClick,
            itemsButtonOnClick = { sendEvent(AppInfoEvent.ToList) },
            itemSelectorButtonOnClick = { sendEvent(AppInfoEvent.ToSelector) },
            qrResultsButtonOnClick = { sendEvent(AppInfoEvent.ToQRResults) },
            viewStyleGuideOnClick = { sendEvent(AppInfoEvent.ToStyleGuide) },
            imageAnalysisButtonOnClick = { sendEvent(AppInfoEvent.ToImageAnalysis) },
            bluetoothButtonOnClick = { sendEvent(AppInfoEvent.ToBluetooth) }
        )
    }
}
