package io.alexeychurchill.clown.library.domain

interface PathCodec {

    fun encode(rawPath: String): String

    fun decode(encodedPath: String): String
}
