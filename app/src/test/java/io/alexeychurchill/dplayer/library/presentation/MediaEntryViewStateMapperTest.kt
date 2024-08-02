package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class MediaEntryViewStateMapperTest {

    private val titleMapperMock: MediaEntryTitleMapper = mockk(relaxed = true)
    private val typeMapperMock: MediaEntryTypeMapper = mockk(relaxed = true)
    private val statusMapperMock: MediaEntryStatusMapper = mockk(relaxed = true)
    private val childInfoMapperMock: DirectoryEntryChildInfoMapper = mockk(relaxed = true)
    private val fileExtensionMapperMock: EntryFileExtensionMapper = mockk(relaxed = true)
    private val coverArtPathMapper: CoverArtPathMapper = mockk(relaxed = true)

    private val mapper = MediaEntryViewStateMapper(
        titleMapper = titleMapperMock,
        typeMapper = typeMapperMock,
        statusMapper = statusMapperMock,
        childInfoMapper = childInfoMapperMock,
        fileExtensionMapper = fileExtensionMapperMock,
        coverArtPathMapper = coverArtPathMapper,
    )

    @Test
    fun `media entry mappers calls dispatched correctly`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.File(path = "", name = FileName.Unknown, extension = null),
        )

        mapper.mapToViewState(entry)

        verify {
            titleMapperMock.mapToTitle(entry)
            typeMapperMock.mapToType(entry)
            statusMapperMock.mapToStatus(entry)
            childInfoMapperMock.mapToChildInfo(entry)
            fileExtensionMapperMock.mapToExtension(entry)
            coverArtPathMapper.mapToCoverArtPath(entry)
        }
    }
}
