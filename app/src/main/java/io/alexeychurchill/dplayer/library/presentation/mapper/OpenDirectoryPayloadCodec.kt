package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.library.presentation.model.OpenDirectoryPayload

interface OpenDirectoryPayloadCodec {

    suspend fun encode(payload: OpenDirectoryPayload): String

    suspend fun decode(encoded: String): OpenDirectoryPayload
}
