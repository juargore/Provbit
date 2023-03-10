package ai.bright.provbit.util

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.data
import platform.Foundation.dataWithBytes

fun ByteArray.NSData(): NSData = this.usePinned {
    if (it.get().isEmpty()) {
        NSData.data()
    } else {
        NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong())
    }
}

fun NSData.ByteArray(): ByteArray {
    return this.bytes?.readBytes(this.length.toInt()) ?: ByteArray(0)
}
