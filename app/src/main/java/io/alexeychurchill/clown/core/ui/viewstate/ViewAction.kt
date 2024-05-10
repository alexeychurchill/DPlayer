package io.alexeychurchill.clown.core.ui.viewstate

typealias ViewActionHandler = (ViewAction) -> Unit

/**
 * An **action**, which can be passed from the **view** to the
 * **presentation logic**
 */
interface ViewAction

/**
 * Kind of default [ViewAction]. For stubs
 */
data object Noop : ViewAction
