package io.alexeychurchill.clown.library.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.alexeychurchill.clown.library.viewstate.LibraryListItemMeta
import io.alexeychurchill.clown.library.viewstate.LibraryListItemState
import io.alexeychurchill.clown.library.viewstate.LibraryListItemState.Entry.Status
import io.alexeychurchill.clown.library.viewstate.LibraryListItemState.Entry.Type

internal class LibraryListItemEntryPreviewProvider :
    PreviewParameterProvider<LibraryListItemState.Entry> {

    override val values: Sequence<LibraryListItemState.Entry> get() {
        val titleIterator = iterator {
            val titleSequence = generateSequence(seed = 1, nextFunction = Int::inc).map {
                "Library Item $it"
            }
            yieldAll(titleSequence)
        }

        val typeSequence = sequenceOf(
            Type.None,
            Type.Directory,
            Type.MusicFile,
        )

        val statusSequence = Status.entries.asSequence()

        val metas = listOf(
                LibraryListItemMeta.DirectoryCount(count = 1),
                LibraryListItemMeta.MusicFileCount(count = 1),
        )

        return typeSequence.flatMap { itemType ->
            statusSequence.map { itemStatus ->
                LibraryListItemState.Entry(
                    title = titleIterator.next(),
                    type = itemType,
                    status = itemStatus,
                    metaItems = metas,
                )
            }
        }
    }
}