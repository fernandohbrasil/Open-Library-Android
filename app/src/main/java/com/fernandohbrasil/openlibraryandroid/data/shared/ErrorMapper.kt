package com.fernandohbrasil.openlibraryandroid.data.shared

import com.fernandohbrasil.openlibraryandroid.domain.shared.model.DomainError
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toDomainError(): DomainError {
    return when (this) {
        is HttpException -> DomainError.Http(code(), null)
        is JsonDataException,
        is JsonEncodingException,
        is EOFException -> DomainError.Serialization
        is UnknownHostException,
        is SocketTimeoutException,
        is ConnectException,
        is ConnectionShutdownException -> DomainError.Network
        else -> DomainError.Unknown(this)
    }
}
