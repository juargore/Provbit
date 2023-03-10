package ai.bright.provbit.demo.entities

/**
 * This is the app-representation of a cloud-side hardware record.
 *
 * When on-boarding a new device, this record is retrieved from the cloud
 * based on information that was recorded at manufacturing time.
 *
 */
data class HardwareRecord(
    val publicKey: String,
    val serialNumber: SerialNumber,
    val hardwareId: HardwareId,
    // lastUpdated // this will be some type of timestamp.
)