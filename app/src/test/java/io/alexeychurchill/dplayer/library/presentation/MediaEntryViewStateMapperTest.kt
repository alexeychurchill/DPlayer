package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class MediaEntryViewStateMapperTest {

    private val pathMapperMock: MediaEntryPathMapper = mockk(relaxed = true)
    private val titleMapperMock: MediaEntryTitleMapper = mockk(relaxed = true)
    private val typeMapperMock: MediaEntryTypeMapper = mockk(relaxed = true)
    private val statusMapperMock: MediaEntryStatusMapper = mockk(relaxed = true)
    private val childInfoMapperMock: DirectoryEntryChildInfoMapper = mockk(relaxed = true)

    private val mapper = MediaEntryViewStateMapper(
        pathMapper = pathMapperMock,
        titleMapper = titleMapperMock,
        typeMapper = typeMapperMock,
        statusMapper = statusMapperMock,
        childInfoMapper = childInfoMapperMock,
    )

    @Test
    fun `media entry mappers calls dispatched correctly`() {
        val entry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(path = "", name = FileName.Unknown),
        )

        mapper.mapToViewState(entry)

        verify {
            pathMapperMock.mapToPath(entry)
            titleMapperMock.mapToTitle(entry)
            typeMapperMock.mapToType(entry)
            statusMapperMock.mapToStatus(entry)
            childInfoMapperMock.mapToChildInfo(entry)
        }
    }
}
