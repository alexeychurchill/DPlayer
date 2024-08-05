package io.alexeychurchill.dplayer.library.presentation

interface OpenDirectoryPayloadCodec {

    suspend fun encode(payload: OpenDirectoryPayload): String

    suspend fun decode(encoded: String): OpenDirectoryPayload
}
