package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Status
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Type
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import javax.inject.Inject


class MediaEntryViewStateMapper @Inject constructor(
    private val titleMapper: MediaEntryTitleMapper,
    private val typeMapper: MediaEntryTypeMapper,
    private val statusMapper: MediaEntryStatusMapper,
    private val childInfoMapper: DirectoryEntryChildInfoMapper,
    private val fileExtensionMapper: EntryFileExtensionMapper,
    private val coverArtPathMapper: CoverArtPathMapper,
) {

    fun mapToViewState(entry: MediaEntry): MediaEntryItemViewState {
        return MediaEntryItemViewState(
            path = entry.fsEntry.path,
            title = titleMapper.mapToTitle(entry),
            type = typeMapper.mapToType(entry),
            status = statusMapper.mapToStatus(entry),
            directoryChildInfo = childInfoMapper.mapToChildInfo(entry),
            fileExtension = fileExtensionMapper.mapToExtension(entry),
            coverArtPath = coverArtPathMapper.mapToCoverArtPath(entry),
        )
    }
}

class MediaEntryTitleMapper @Inject constructor() {

    fun mapToTitle(entry: MediaEntry): String = when (entry.fsEntry) {
        is FileSystemEntry.Directory -> {
            (entry.source as? EntrySource.UserLibrary)?.aliasTitle
                ?: (entry.fsEntry.name as? FileName.Name)?.value
                ?: FileName.DefaultUnknownValue
        }

        is FileSystemEntry.File -> {
            (entry.fsEntry.name as? FileName.Name)?.value ?: FileName.DefaultUnknownValue
        }
    }
}

class MediaEntryTypeMapper @Inject constructor() {

    fun mapToType(entry: MediaEntry): Type = when (entry.fsEntry) {
        is FileSystemEntry.Directory -> Type.Directory
        is FileSystemEntry.File -> Type.MusicFile
    }
}

class MediaEntryStatusMapper @Inject constructor() {

    fun mapToStatus(entry: MediaEntry): Status {
        val fsEntry = entry.fsEntry
        if (fsEntry !is FileSystemEntry.Directory) {
            return Status.None
        }

        val directoryExists = fsEntry.exists
        val directoryHasChildren = (entry.info as? EntryInfo.Directory)
            ?.let { (it.directoryCount + it.musicFileCount) > 0 }
            ?: false

        return when {
            !directoryExists -> Status.Faulty

            directoryExists && directoryHasChildren -> Status.Openable

            else -> Status.None
        }
    }
}

class DirectoryEntryChildInfoMapper @Inject constructor() {

    fun mapToChildInfo(entry: MediaEntry): DirectoryChildInfoViewState? {
        if (entry.info !is EntryInfo.Directory) return null
        return DirectoryChildInfoViewState(
            subDirectoryCount = entry.info.directoryCount,
            fileCount = entry.info.musicFileCount,
        )
    }
}

class EntryFileExtensionMapper @Inject constructor() {

    fun mapToExtension(entry: MediaEntry): String? {
        return (entry.fsEntry as? FileSystemEntry.File)?.extension
    }
}

class CoverArtPathMapper @Inject constructor() {

    fun mapToCoverArtPath(entry: MediaEntry): CoverArtPath? {
        if (entry.fsEntry !is FileSystemEntry.File) {
            return null
        }

        return CoverArtPath.LocalUri(mediaUri = entry.fsEntry.path)
    }
}
