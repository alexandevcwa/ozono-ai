package com.ozono.repository

import com.ozono.network.AuthNetwork
import com.ozono.network.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun authRepository(
        authNetwork: AuthNetwork,
        tokenInterceptor: TokenInterceptor
    ): AuthRepository {
        return AuthRepositoryImpl(authNetwork, tokenInterceptor)
    }

}