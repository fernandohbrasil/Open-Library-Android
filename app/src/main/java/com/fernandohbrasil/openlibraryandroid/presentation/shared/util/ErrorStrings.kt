package com.fernandohbrasil.openlibraryandroid.presentation.shared.util

import androidx.annotation.StringRes
import com.fernandohbrasil.openlibraryandroid.R
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.DomainError

@StringRes
fun DomainError.toUserMessageRes(): Int {
    return when (this) {
        is DomainError.Network -> R.string.shared_error_network
        is DomainError.Http -> R.string.shared_error_http
        is DomainError.Serialization -> R.string.shared_error_serialization
        is DomainError.Unknown -> R.string.shared_error_unknown
    }
}
