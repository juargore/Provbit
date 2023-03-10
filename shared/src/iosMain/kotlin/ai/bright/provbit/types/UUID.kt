package ai.bright.provbit.types

import platform.Foundation.NSUUID

actual fun uuidString(): String = NSUUID().UUIDString.lowercase()
