package ai.bright.provbit.architecture

/**
 * Serves to cross architectural boundaries à la Clean Architecture.
 *
 * Usually an converter will be created in one direction between an outer
 * data object (such as a library return value, a network response, or
 * database query results) and an inner data object (like a business object).
 *
 * In many cases, an converter needs only convert in one direction, as many
 * external framework layers will use separate input and output models.
 *
 */
interface Converter<OuterType, InnerType> {
    fun convertOutbound(value: InnerType): OuterType = throw NotImplementedError()
    fun convertInbound(value: OuterType): InnerType = throw NotImplementedError()
}

fun <OuterType, InnerType> Converter<OuterType, InnerType>.mapOutbound(values: List<InnerType>): List<OuterType> =
    values.map { this.convertOutbound(it) }

fun <OuterType, InnerType> Converter<OuterType, InnerType>.mapInbound(values: List<OuterType>): List<InnerType> =
    values.map { this.convertInbound(it) }
