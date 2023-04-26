package com.example.assesmentanywhererealestate.di

import com.example.assesmentanywhererealestate.BuildConfig
import com.example.assesmentanywhererealestate.data.model.NetworkDataSource
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkDatasource(): NetworkDataSource {
        val baseUrl = BuildConfig.BACKEND_URL//"https://api.duckduckgo.com"
        //val openAPISecretKey = "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"
        val client: OkHttpClient = OkHttpClient.Builder()
            //.readTimeout(timeout = 50000, TimeUnit.MILLISECONDS)
            //.addInterceptor(AuthInterceptor(openAPISecretKey))
            .build()
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()
        return retrofit.create(NetworkDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }


}