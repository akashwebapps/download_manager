package com.example.download_manager.app.domain.repository

import com.example.download_manager.core.domain.util.NetworkError
import com.example.download_manager.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.ByteReadChannel

interface NetworkRepository {

    suspend fun downloadFile(url : String) : Result<ByteReadChannel,NetworkError>


}