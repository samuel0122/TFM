package com.mastermovilesua.persistencia.tfm_detectorpentagramas.di

import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiffUtils {

    @Singleton
    @Provides
    fun provideBookItemDiffUtil(): DiffUtil.ItemCallback<BookItem> =
        object : DiffUtil.ItemCallback<BookItem>() {
            override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
                oldItem == newItem
        }

    @Singleton
    @Provides
    fun providePageItemDiffUtil(): DiffUtil.ItemCallback<PageItem> =
        object : DiffUtil.ItemCallback<PageItem>() {
            override fun areItemsTheSame(oldItem: PageItem, newItem: PageItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PageItem, newItem: PageItem): Boolean =
                (oldItem.id == newItem.id) && (oldItem.imageUri == newItem.imageUri)
        }
}