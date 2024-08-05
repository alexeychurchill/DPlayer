package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class SecondaryInfoMapperTest {

    private val trackInfoMapperMock: TrackInfoMapper = mockk(relaxed = true)
    private val directoryChildInfoMapperMock: DirectoryEntryChildInfoMapper = mockk(relaxed = true)

    private val mapper = SecondaryInfoMapper(
        trackInfoMapper = trackInfoMapperMock,
        directoryChildInfoMapper = directoryChildInfoMapperMock,
    )

    @Test
    fun `mapper calls track info mapper for the file entry`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.File(
                path = "", name = FileName.Unknown, extension = null,
            ),
        )

        mapper.mapToSecondaryInfo(entry, metadata = null)

        verify { trackInfoMapperMock.mapToTrackInfo(metadata = null) }
        verify(exactly = 0) { directoryChildInfoMapperMock.mapToChildInfo(entry) }
    }

    @Test
    fun `mapper calls directory child info mapper for the directory entry`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "", name = FileName.Unknown, exists = true,
            ),
        )

        mapper.mapToSecondaryInfo(entry, metadata = null)

        verify(exactly = 0) { trackInfoMapperMock.mapToTrackInfo(metadata = null) }
        verify { directoryChildInfoMapperMock.mapToChildInfo(entry) }
    }
}
