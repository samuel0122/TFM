package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BookEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity

data class BookWithPagesRelation (
    @Embedded val book: BookEntity,
    @Relation(
        entity = PageEntity::class,
        parentColumn = MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID,
        entityColumn = MusicScoreBooksContract.TABLE_PAGE_COLUMN_BOOK_ID
    )
    val pages: List<PageEntity>
)