package com.mastermovilesua.persistencia.tfm_detectorpentagramas.di

import android.content.Context
import androidx.room.Room
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.AppDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, MusicScoreBooksContract.DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideBookDao(db: AppDatabase) = db.bookDao()


    @Singleton
    @Provides
    fun providePageDao(db: AppDatabase) = db.pageDao()


    @Singleton
    @Provides
    fun provideBoxDao(db: AppDatabase) = db.boxDao()
}