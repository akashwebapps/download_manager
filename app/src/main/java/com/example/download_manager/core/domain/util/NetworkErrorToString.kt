package com.example.download_manager.core.domain.util


fun NetworkError.toResString() : String{
    return when(this){
        NetworkError.REQUEST_TIMEOUT -> "Network request timeout"
        NetworkError.TOO_MANY_REQUESTS -> "Too many requests"
        NetworkError.NO_INTERNET -> "No internet connection"
        NetworkError.SERVER_ERROR -> "Server error"
        NetworkError.SERIALIZATION -> "Serialization error"
        NetworkError.UNKNOWN -> "Unknown error"
    }
}