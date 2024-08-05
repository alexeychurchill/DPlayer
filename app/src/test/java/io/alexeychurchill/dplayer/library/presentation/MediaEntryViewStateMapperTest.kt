package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class MediaEntryViewStateMapperTest {

    private val titleMapperMock: MediaEntryTitleMapper = mockk(relaxed = true)
    private val typeMapperMock: MediaEntryTypeMapper = mockk(relaxed = true)
    private val statusMapperMock: MediaEntryStatusMapper = mockk(relaxed = true)
    private val fileExtensionMapperMock: EntryFileExtensionMapper = mockk(relaxed = true)
    private val coverArtPathMapperMock: CoverArtPathMapper = mockk(relaxed = true)
    private val secondaryInfoMapperMock: SecondaryInfoMapper = mockk(relaxed = true)
    private val openActionMapperMock: MediaEntryOpenActionMapper = mockk(relaxed = true)

    private val mapper = MediaEntryViewStateMapper(
        titleMapper = titleMapperMock,
        typeMapper = typeMapperMock,
        statusMapper = statusMapperMock,
        fileExtensionMapper = fileExtensionMapperMock,
        coverArtPathMapper = coverArtPathMapperMock,
        secondaryInfoMapper = secondaryInfoMapperMock,
        openActionMapper = openActionMapperMock,
    )

    @Test
    fun `media entry mappers calls dispatched correctly`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.File(path = "", name = FileName.Unknown, extension = null),
        )
        val metadata = FileMetadata()

        mapper.mapToViewState(entry, metadata)

        verify {
            titleMapperMock.mapToTitle(entry, metadata)
            typeMapperMock.mapToType(entry)
            statusMapperMock.mapToStatus(entry)
            fileExtensionMapperMock.mapToExtension(entry)
            coverArtPathMapperMock.mapToCoverArtPath(entry)
            secondaryInfoMapperMock.mapToSecondaryInfo(entry, metadata)
            openActionMapperMock.mapToOpenAction(entry)
        }
    }
}
