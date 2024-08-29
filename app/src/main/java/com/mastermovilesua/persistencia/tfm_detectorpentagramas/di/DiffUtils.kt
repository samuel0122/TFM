package com.mastermovilesua.persistencia.tfm_detectorpentagramas.di

import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
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
    fun provideBookItemDiffUtil(): DiffUtil.ItemCallback<BookWithPagesItem> =
        object : DiffUtil.ItemCallback<BookWithPagesItem>() {
            override fun areItemsTheSame(
                oldItem: BookWithPagesItem,
                newItem: BookWithPagesItem
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: BookWithPagesItem,
                newItem: BookWithPagesItem
            ): Boolean =
                oldItem.book == newItem.book &&
                        oldItem.pages.firstOrNull()?.imageUri == newItem.pages.firstOrNull()?.imageUri
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