package com.ozono.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun tokenInterceptor(): TokenInterceptor {
        return TokenInterceptor()
    }

    @Provides
    @Singleton
    fun okHttpClient(authInterceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson: Gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .create()
        return Retrofit.Builder()
            .baseUrl("https://ozono.alexandevcwa.tech/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun authenticationNetwork(retrofit: Retrofit): AuthNetwork {
        return retrofit.create(AuthNetwork::class.java)
    }

    @Provides
    @Singleton
    fun analyzerNetwork(retrofit: Retrofit): AnalyzerNetwork {
        return retrofit.create(AnalyzerNetwork::class.java)
    }
}