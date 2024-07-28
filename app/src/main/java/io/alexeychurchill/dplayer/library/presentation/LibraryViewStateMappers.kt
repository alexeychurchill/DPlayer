package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.library.domain.DirectorySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Status
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Type
import javax.inject.Inject


class MediaEntryViewStateMapper @Inject constructor(
    private val pathMapper: MediaEntryPathMapper,
    private val titleMapper: MediaEntryTitleMapper,
    private val typeMapper: MediaEntryTypeMapper,
    private val statusMapper: MediaEntryStatusMapper,
    private val childInfoMapper: DirectoryEntryChildInfoMapper,
) {

    fun mapToViewState(entry: MediaEntry): MediaEntryItemViewState {
        return MediaEntryItemViewState(
            path = pathMapper.mapToPath(entry),
            title = titleMapper.mapToTitle(entry),
            type = typeMapper.mapToType(entry),
            status = statusMapper.mapToStatus(entry),
            directoryChildInfo = childInfoMapper.mapToChildInfo(entry),
        )
    }
}

class MediaEntryPathMapper @Inject constructor() {

    fun mapToPath(entry: MediaEntry): String? = when (entry) {
        is MediaEntry.Directory -> entry.directoryEntry?.path
        is MediaEntry.File -> entry.fileEntry.path
    }
}

class MediaEntryTitleMapper @Inject constructor() {

    fun mapToTitle(entry: MediaEntry): String = when (entry) {
        is MediaEntry.Directory -> {
            (entry.source as? DirectorySource.FromUserLibrary)?.aliasTitle
                ?: (entry.directoryEntry?.name as? FileName.Name)?.value
                ?: FileName.DefaultUnknownValue
        }

        is MediaEntry.File -> {
            (entry.fileEntry.name as? FileName.Name)?.value ?: FileName.DefaultUnknownValue
        }
    }
}

class MediaEntryTypeMapper @Inject constructor() {

    fun mapToType(entry: MediaEntry): Type = when (entry) {
        is MediaEntry.Directory -> Type.Directory
        is MediaEntry.File -> Type.MusicFile
    }
}

class MediaEntryStatusMapper @Inject constructor() {

    fun mapToStatus(entry: MediaEntry): Status {
        if (entry !is MediaEntry.Directory) {
            return Status.None
        }

        val directoryExists = entry.directoryEntry?.exists == true
        val directoryHasChildren = (entry.subDirectoryCount + entry.musicFileCount) > 0
        return when {
            !directoryExists -> Status.Faulty

            directoryExists && directoryHasChildren -> Status.Openable

            else -> Status.None
        }
    }
}

class DirectoryEntryChildInfoMapper @Inject constructor() {

    fun mapToChildInfo(entry: MediaEntry): DirectoryChildInfoViewState? {
        if (entry !is MediaEntry.Directory) return null
        return DirectoryChildInfoViewState(
            subDirectoryCount = entry.subDirectoryCount,
            fileCount = entry.musicFileCount,
        )
    }
}
