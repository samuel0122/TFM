package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract

@Entity(
    tableName = MusicScoreBooksContract.TABLE_PAGE,
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = [MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID],
        childColumns = [MusicScoreBooksContract.TABLE_PAGE_COLUMN_BOOK_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID)
    val pageId: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_PAGE_COLUMN_IMAGE_URI)
    val imageUri: String,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_PAGE_COLUMN_PROCESSED)
    var processed: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_PAGE_COLUMN_ORDER)
    var order: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_PAGE_COLUMN_BOOK_ID)
    val bookId: Int
)

