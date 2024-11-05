package com.example.download_manager.core.domain.util

typealias DomainError = Error
typealias DomainResult<D,T> = Result<D,T>

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class OnLoading<out P>(val progress: P): Result<P, Nothing>
    data class Error<out E: DomainError>(val error: E): Result<Nothing, E>
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
        is Result.OnLoading -> Result.OnLoading(map(progress))
    }
}

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }

        is Result.OnLoading -> this
    }
}
inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
        is Result.OnLoading -> this

    }
}

inline fun <P, E: Error> Result<P, E>.onLoading(action: (P) -> Unit): Result<P, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> this
        is Result.OnLoading -> {
            action(progress)
            this
        }

    }
}

typealias EmptyResult<E> = Result<Unit, E>