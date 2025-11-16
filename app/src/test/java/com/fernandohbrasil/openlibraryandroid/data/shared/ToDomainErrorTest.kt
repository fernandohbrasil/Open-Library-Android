package com.fernandohbrasil.openlibraryandroid.data.shared

import com.fernandohbrasil.openlibraryandroid.domain.shared.model.DomainError
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import okhttp3.internal.http2.ConnectionShutdownException
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ToDomainErrorTest {

    @Test
    fun toDomainError_should_returnHttpError_when_httpException() {
        val response = Response.error<Any>(404, okhttp3.ResponseBody.create(null, ""))
        val httpException = HttpException(response)

        val result = httpException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Http::class.java)
        assertThat((result as DomainError.Http).code).isEqualTo(404)
    }

    @Test
    fun toDomainError_should_returnSerializationError_when_jsonDataException() {
        val jsonDataException = JsonDataException("Invalid JSON")

        val result = jsonDataException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Serialization::class.java)
    }

    @Test
    fun toDomainError_should_returnSerializationError_when_jsonEncodingException() {
        val jsonEncodingException = JsonEncodingException("Encoding error")

        val result = jsonEncodingException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Serialization::class.java)
    }

    @Test
    fun toDomainError_should_returnSerializationError_when_eofException() {
        val eofException = EOFException("Unexpected end of file")

        val result = eofException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Serialization::class.java)
    }

    @Test
    fun toDomainError_should_returnNetworkError_when_unknownHostException() {
        val unknownHostException = UnknownHostException("Host not found")

        val result = unknownHostException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Network::class.java)
    }

    @Test
    fun toDomainError_should_returnNetworkError_when_socketTimeoutException() {
        val socketTimeoutException = SocketTimeoutException("Connection timeout")

        val result = socketTimeoutException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Network::class.java)
    }

    @Test
    fun toDomainError_should_returnNetworkError_when_connectException() {
        val connectException = ConnectException("Connection refused")

        val result = connectException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Network::class.java)
    }

    @Test
    fun toDomainError_should_returnNetworkError_when_connectionShutdownException() {
        val connectionShutdownException = ConnectionShutdownException()

        val result = connectionShutdownException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Network::class.java)
    }

    @Test
    fun toDomainError_should_returnUnknownError_when_otherException() {
        val runtimeException = RuntimeException("Generic error")

        val result = runtimeException.toDomainError()

        assertThat(result).isInstanceOf(DomainError.Unknown::class.java)
        assertThat((result as DomainError.Unknown).cause).isEqualTo(runtimeException)
    }
}
