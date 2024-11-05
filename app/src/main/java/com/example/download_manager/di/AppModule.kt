package com.example.download_manager.di

import com.example.download_manager.app.data.RepositoryImpl
import com.example.download_manager.app.domain.repository.NetworkRepository
import com.example.download_manager.app.presentation.AppViewModel
import com.example.download_manager.core.data.networking.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object AppModule {

    val appModule = module {
        single { HttpClientFactory.createHttpClientEngine(CIO.create()) }
        singleOf(::RepositoryImpl).bind<NetworkRepository>()
        viewModel { AppViewModel(get(),get()) }
    }


}