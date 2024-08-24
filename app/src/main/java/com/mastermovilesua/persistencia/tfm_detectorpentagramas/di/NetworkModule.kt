package com.mastermovilesua.persistencia.tfm_detectorpentagramas.di

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.PageApiClient
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PageApiContract.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePageApiClient(retrofit: Retrofit): PageApiClient {
        return retrofit.create(PageApiClient::class.java)
    }
}