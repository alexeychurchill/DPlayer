package io.alexeychurchill.clown.core.viewstate

typealias ViewActionHandler = (ViewAction) -> Unit

class ViewAction(
    private val key: Any? = null,
    private val block: suspend () -> Unit,
) {

    companion object {

        fun noop(): ViewAction = ViewAction(block = { })

        fun todo(): ViewAction = ViewAction(block = { TODO() })
    }

    override fun equals(other: Any?): Boolean = other is ViewAction && other.key == key

    override fun hashCode(): Int = key?.hashCode() ?: 0

    suspend operator fun invoke(): Unit = block()
}
