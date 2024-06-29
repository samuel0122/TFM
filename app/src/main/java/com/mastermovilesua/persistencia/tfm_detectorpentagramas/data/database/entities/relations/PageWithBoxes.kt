package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity

data class PageWithBoxes (
    @Embedded val page: PageEntity,
    @Relation(
        entity = BoxEntity::class,
        parentColumn = MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID,
        entityColumn = MusicScoreBooksContract.TABLE_BOX_COLUMN_PAGE_ID
    )
    val boxes: List<BoxEntity>
)