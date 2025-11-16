package com.fernandohbrasil.openlibraryandroid.data.shared

import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import io.reactivex.rxjava3.core.Single

fun <T : Any> Single<T>.toApiResult(): Single<ApiResult<T>> {
    return this
        .map<ApiResult<T>> { ApiResult.Success(it) }
        .onErrorReturn { ApiResult.Error(it.toDomainError()) }
}
