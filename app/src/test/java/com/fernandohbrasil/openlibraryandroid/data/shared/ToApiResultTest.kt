package com.fernandohbrasil.openlibraryandroid.data.shared

import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.DomainError
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import java.net.UnknownHostException

class ToApiResultTest {

    @Test
    fun toApiResult_should_returnSuccess_when_singleEmitsValue() {
        val testData = "test data"
        val single: Single<String> = Single.just(testData)

        val testObserver: TestObserver<ApiResult<String>> = single.toApiResult().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo(testData)
    }

    @Test
    fun toApiResult_should_returnError_when_singleEmitsError() {
        val throwable = UnknownHostException("Network error")
        val single: Single<String> = Single.error(throwable)

        val testObserver: TestObserver<ApiResult<String>> = single.toApiResult().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat((result as ApiResult.Error).error).isInstanceOf(DomainError.Network::class.java)
    }

    @Test
    fun toApiResult_should_mapErrorToDomainError() {
        val throwable = RuntimeException("Generic error")
        val single: Single<String> = Single.error(throwable)

        val testObserver: TestObserver<ApiResult<String>> = single.toApiResult().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        val error = (result as ApiResult.Error).error
        assertThat(error).isInstanceOf(DomainError.Unknown::class.java)
    }
}
