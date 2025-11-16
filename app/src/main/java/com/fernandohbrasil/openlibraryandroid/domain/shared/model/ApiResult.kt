package com.fernandohbrasil.openlibraryandroid.domain.shared.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: DomainError) : ApiResult<Nothing>()
}
