package com.example.download_manager.app.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.download_manager.app.domain.repository.NetworkRepository
import com.example.download_manager.core.domain.util.NetworkError
import com.example.download_manager.core.domain.util.Result
import com.example.download_manager.core.domain.util.onError
import com.example.download_manager.core.domain.util.onLoading
import com.example.download_manager.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class AppViewModel(
    private val networkRepository: NetworkRepository,
    private val httpClient: HttpClient
) : ViewModel() {

    init {
        Log.d("AppViewModel", "init start: Viewmodel has been initialize")

        viewModelScope.launch(Dispatchers.IO) {
            download()


        }


    }


    private suspend fun download() {
        httpClient.prepareGet(
            urlString = "https://videos.pexels.com/video-files/3195394/3195394-uhd_3840_2160_25fps.mp4",
            block = {
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
            })
            .execute { response ->
            when (response.status.value) {
                in 200..299 -> {
                    try {
                        val byteReadChannel = response.bodyAsChannel()
                        Log.d("AppViewModel", "success : ${byteReadChannel.totalBytesRead}")

                    } catch (e: NoTransformationFoundException) {
                        Log.d("AppViewModel", "success : ${e.message}")


                    }
                }

                /* 408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
                 429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
                 in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)*/
                else -> Log.d("AppViewModel", "success : UNKNOWN ERROR")

            }


        }
    }


}