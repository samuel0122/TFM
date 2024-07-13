package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem

@Entity(tableName = MusicScoreBooksContract.TABLE_BOOK)
data class BookEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID)
    val bookId: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOOK_COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOOK_COLUMN_DESCRIPTION)
    var description: String,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOOK_COLUMN_DATASET)
    var dataset: Int,
)

fun BookItem.toDatabase() = BookEntity(bookId, title, description, dataset.value)