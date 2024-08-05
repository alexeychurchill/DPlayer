package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.LibraryAction.OpenMediaEntry
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Status
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Type
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import javax.inject.Inject


class MediaEntryViewStateMapper @Inject constructor(
    private val titleMapper: MediaEntryTitleMapper,
    private val typeMapper: MediaEntryTypeMapper,
    private val statusMapper: MediaEntryStatusMapper,
    private val fileExtensionMapper: EntryFileExtensionMapper,
    private val coverArtPathMapper: CoverArtPathMapper,
    private val secondaryInfoMapper: SecondaryInfoMapper,
    private val openActionMapper: MediaEntryOpenActionMapper,
) {

    fun mapToViewState(
        entry: MediaEntry,
        metadata: FileMetadata? = null,
    ): MediaEntryItemViewState {
        return MediaEntryItemViewState(
            path = entry.fsEntry.path,
            title = titleMapper.mapToTitle(entry, metadata),
            type = typeMapper.mapToType(entry),
            status = statusMapper.mapToStatus(entry),
            secondaryInfo = secondaryInfoMapper.mapToSecondaryInfo(entry, metadata),
            fileExtension = fileExtensionMapper.mapToExtension(entry),
            coverArtPath = coverArtPathMapper.mapToCoverArtPath(entry),
            openAction = openActionMapper.mapToOpenAction(entry),
        )
    }
}

class MediaEntryTitleMapper @Inject constructor() {

    fun mapToTitle(entry: MediaEntry, metadata: FileMetadata?): String = when (entry.fsEntry) {
        is FileSystemEntry.Directory -> {
            (entry.source as? EntrySource.UserLibrary)?.aliasTitle
                ?: (entry.fsEntry.name as? FileName.Name)?.value
                ?: FileName.DefaultUnknownValue
        }

        is FileSystemEntry.File -> {
            metadata?.title
                ?: (entry.fsEntry.name as? FileName.Name)?.value
                ?: FileName.DefaultUnknownValue
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

class SecondaryInfoMapper @Inject constructor(
    private val trackInfoMapper: TrackInfoMapper,
    private val directoryChildInfoMapper: DirectoryEntryChildInfoMapper,
) {

    fun mapToSecondaryInfo(entry: MediaEntry, metadata: FileMetadata?): SecondaryInfoViewState? {
        return when (entry.fsEntry) {
            is FileSystemEntry.File -> trackInfoMapper.mapToTrackInfo(metadata)
            is FileSystemEntry.Directory -> directoryChildInfoMapper.mapToChildInfo(entry)
        }
    }
}

class TrackInfoMapper @Inject constructor() {

    fun mapToTrackInfo(metadata: FileMetadata?): SecondaryInfoViewState.TrackInfo? {
        metadata?.takeIf { it.title != null } ?: return null
        return SecondaryInfoViewState.TrackInfo(
            artist = metadata.artist,
            year = metadata.year,
        )
    }
}

class DirectoryEntryChildInfoMapper @Inject constructor() {

    fun mapToChildInfo(entry: MediaEntry): SecondaryInfoViewState.DirectoryChildInfo? {
        if (entry.info !is EntryInfo.Directory) return null
        return SecondaryInfoViewState.DirectoryChildInfo(
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

class MediaEntryOpenActionMapper @Inject constructor(
    private val titleMapper: MediaEntryTitleMapper,
) {

    fun mapToOpenAction(entry: MediaEntry): OpenMediaEntry = when (entry.fsEntry) {

        is FileSystemEntry.Directory -> OpenMediaEntry.Directory(
            payload = OpenDirectoryPayload(
                path = entry.fsEntry.path,
                title = titleMapper.mapToTitle(entry, metadata = null),
            ),
        )

        is FileSystemEntry.File -> OpenMediaEntry.File(
            path = entry.fsEntry.path,
        )
    }
}
