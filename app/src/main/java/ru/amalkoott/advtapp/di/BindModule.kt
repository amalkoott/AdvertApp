package ru.amalkoott.advtapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.amalkoott.advtapp.data.local.AppRepositoryDB
import ru.amalkoott.advtapp.data.remote.ServerRequestsRepository
import ru.amalkoott.advtapp.domain.AppRemoteRepository
import ru.amalkoott.advtapp.domain.AppRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {
    @Singleton
    @Binds
    fun bindsAppRepository(repo: AppRepositoryDB): AppRepository

    @Singleton
    @Binds
    fun bindRemoteRepository(repo: ServerRequestsRepository): AppRemoteRepository
}