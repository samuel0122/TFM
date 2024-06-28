package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Box
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Page

data class PageWithBoxes (
    @Embedded val page: Page,
    @Relation(
        entity = Box::class,
        parentColumn = MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID,
        entityColumn = MusicScoreBooksContract.TABLE_BOX_COLUMN_PAGE_ID
    )
    val boxes: List<Box>
)