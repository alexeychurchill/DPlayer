package io.alexeychurchill.clown.library.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.alexeychurchill.clown.library.viewstate.LibraryListItemState

@Composable
fun LibraryList(
    items: List<LibraryListItemState>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items.forEach { item ->
            renderItem(item)
        }
    }
}

private fun LazyListScope.renderItem(item: LibraryListItemState) {
    when (item) {
        is LibraryListItemState.Entry -> {
            item {
                EntryLibraryListItem(
                    itemState = item,
                )
            }
        }
    }
}
