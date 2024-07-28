@file:OptIn(ExperimentalEncodingApi::class)

package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.library.domain.PathCodec
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Singleton
class Base64PathCodec @Inject constructor() : PathCodec {

    private companion object {
        val Charset = Charsets.UTF_8
    }

    override fun encode(rawPath: String): String {
        return Base64.encode(rawPath.toByteArray(Charset))
    }

    override fun decode(encodedPath: String): String {
        return Base64.decode(encodedPath).toString(Charset)
    }
}
