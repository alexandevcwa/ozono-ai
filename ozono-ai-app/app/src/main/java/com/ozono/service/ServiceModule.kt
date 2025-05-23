package com.ozono.service

import com.ozono.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun authService(authRepository: AuthRepository): AuthService {
        return AuthServiceImpl(authRepository)
    }

}