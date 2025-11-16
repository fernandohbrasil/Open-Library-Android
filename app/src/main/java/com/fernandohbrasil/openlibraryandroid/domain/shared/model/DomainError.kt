package com.fernandohbrasil.openlibraryandroid.domain.shared.model

sealed class DomainError {
    object Network : DomainError()
    data class Http(val code: Int, val message: String?) : DomainError()
    object Serialization : DomainError()
    data class Unknown(val cause: Throwable?) : DomainError()
}

