package ai.bright.provbit.types

import java.util.UUID

actual fun uuidString(): String = UUID.randomUUID().toString().lowercase()
