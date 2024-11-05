package com.example.download_manager.app.data

import android.util.Log
import com.example.download_manager.app.domain.repository.NetworkRepository
import com.example.download_manager.core.data.networking.safeCall
import com.example.download_manager.core.domain.util.NetworkError
import com.example.download_manager.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import java.io.File
import kotlin.time.Duration.Companion.minutes

class RepositoryImpl(
    private val httpClient: HttpClient
) : NetworkRepository {

    override suspend fun downloadFile(url: String): Result<ByteReadChannel, NetworkError> {
       return httpClient.prepareGet(urlString = url, block = {
            val timeout = 30.minutes.inWholeMilliseconds
            timeout {
                requestTimeoutMillis = timeout
                connectTimeoutMillis = timeout
                socketTimeoutMillis = timeout
            }

            onDownload { bytesSentTotal, contentLength ->
                val progress = (bytesSentTotal.toFloat() / contentLength.toFloat())
                Result.OnLoading(progress = progress)
                Log.d("AppViewModel", "progress : ${progress}")

            }
        }).execute { response ->
          when (response.status.value) {
                in 200..299 -> {
                    try {
                        val byteReadChannel = response.bodyAsChannel()
                        Result.Success(byteReadChannel)
                    } catch (e: NoTransformationFoundException) {
                        Result.Error(NetworkError.SERIALIZATION)

                    }
                }

                408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
                429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
                else -> Result.Error(NetworkError.UNKNOWN)
            }


        }
    }


}
