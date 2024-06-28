package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Book
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Page

data class BookWithPages (
    @Embedded val book: Book,
    @Relation(
        entity = Page::class,
        parentColumn = MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID,
        entityColumn = MusicScoreBooksContract.TABLE_PAGE_COLUMN_BOOK_ID
    )
    val pages: List<Page>
)