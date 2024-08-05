@file:OptIn(ExperimentalEncodingApi::class)

package io.alexeychurchill.dplayer.library.data

import io.alexeychurchill.dplayer.library.presentation.OpenDirectoryPayload
import io.alexeychurchill.dplayer.library.presentation.OpenDirectoryPayloadCodec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Singleton
class Base64OpenDirectoryPayloadCodec @Inject constructor(
    private val json: Json,
) : OpenDirectoryPayloadCodec {

    private companion object {
        val Charset = Charsets.UTF_8
    }

    override suspend fun encode(payload: OpenDirectoryPayload): String {
        return withContext(Dispatchers.Default) {
            val jsonString = json.encodeToString(payload)
            Base64.encode(jsonString.toByteArray(Charset))
        }
    }

    override suspend fun decode(encoded: String): OpenDirectoryPayload {
        return withContext(Dispatchers.Default) {
            val jsonString = Base64.decode(encoded).toString(Charset)
            json.decodeFromString(jsonString)
        }
    }
}
