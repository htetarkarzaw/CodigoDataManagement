package com.htetarkarzaw.datamanagement.di

import android.content.Context
import com.htetarkarzaw.datamanagement.data.repository.MainRepositoryImpl
import com.htetarkarzaw.datamanagement.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepoModule {
    @Provides
    @Singleton
    fun provideMainRepository(
        @ApplicationContext context: Context
    ): MainRepository {
        return MainRepositoryImpl(context = context)
    }
}