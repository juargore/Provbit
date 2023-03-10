package ai.bright.provbit

import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.create

class Greeter {
    fun greet(subject: String): String {
        val paddedSubject = if (subject.isBlank()) "" else " $subject"
        return "Hello$paddedSubject, this is KMM."
    }
}

