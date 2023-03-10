package ai.bright.provbit.logging

import ai.bright.provbit.client.conch.network.NetworkLogger
import ai.bright.provbit.client.conch.network.RequestMethod
import ai.bright.provbit.client.conch.network.Response
import kotlin.math.max

// This should maybe be moved to our generator in the future.
class ConsoleNetworkLogger(private val out: (String) -> Unit) : NetworkLogger {

    override fun request(
        method: RequestMethod,
        absoluteUrl: String,
        headers: Map<String, String>,
        body: ByteArray?
    ) {
        "REQUEST: ${method.name.uppercase()} $absoluteUrl".printIndent(level = 1)
        "HEADERS:".printIndent(level = 2)
        printHeaders(headers, level = 3)
        "".printIndent(level = 0)
        "BODY".printIndent(level = 2)
        bodyStringFor(headers, body).printIndent(level = 3)
        "".printIndent(level = 0)
    }

    override fun response(
        method: RequestMethod,
        absoluteUrl: String,
        failureDescription: String?,
        response: Response?
    ) {
        "RESPONSE: ${method.name.uppercase()} $absoluteUrl".printIndent(level = 1)

        if (failureDescription != null) {
            "FAILURE: $failureDescription".printIndent(level = 2)
            "".printIndent(level = 0)
        }

        if (response != null) {
            "CODE: ${response.code}".printIndent(level = 2)
            "".printIndent(level = 0)
            "HEADERS:".printIndent(level = 3)
            printHeaders(response.headers, level = 3)
            "".printIndent(level = 0)
            "BODY".printIndent(level = 2)
            bodyStringFor(response.headers, response.body).printIndent(level = 3)
            "".printIndent(level = 0)
        }
    }

    private fun String.truncateNetResponse(
        length: Int = 50000,
        suffix: String = "..."
    ): String = this.truncate(length, suffix)

    // Indent level 0 is baseline. Headers are level 1
    private fun String.printIndent(level: Int) =
        out("|" + (if (level > 0) " " else "") + ("  ".repeat(max(0, level - 1))) + this)

    // Test if the header contains the json content-type
    private fun Map<String, String>.isJsonContent(): Boolean =
        this.mapKeys { it.key.lowercase() }["content-type"]?.contains("application/json") ?: false

    private fun bodyStringFor(
        headers: Map<String, String>,
        body: ByteArray?
    ): String = if (body == null) {
        "<no body>"
    } else {
        if (headers.isJsonContent()) {
            try {
                body.decodeToString()
            } catch (e: Throwable) {
                "<error deserializing>"
            }
        } else {
            "<binary body>"
        }
    }

    private fun printHeaders(
        headers: Map<String, String>,
        level: Int = 2
    ) {
        if (headers.isEmpty()) {
            "<no headers>".printIndent(level = level)
        } else {
            headers.forEach { header ->
                "${header.key}: ${header.value.truncateNetResponse(50)}".printIndent(level = level)
            }
        }
    }
}

fun String.truncate(
    length: Int,
    suffix: String = "..."
): String {
    return if (this.length <= length) {
        this
    } else {
        this.substring(
            startIndex = 0,
            endIndex = length - suffix.length
        ) + suffix
    }
}
