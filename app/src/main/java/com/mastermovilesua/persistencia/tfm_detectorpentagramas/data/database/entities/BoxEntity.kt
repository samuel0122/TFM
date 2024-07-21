package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem

@Entity(
    tableName = MusicScoreBooksContract.TABLE_BOX,
    foreignKeys = [ForeignKey(
        entity = PageEntity::class,
        parentColumns = [MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID],
        childColumns = [MusicScoreBooksContract.TABLE_BOX_COLUMN_PAGE_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BoxEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOX_COLUMN_BOX_ID)
    val boxId: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOX_COLUMN_X)
    var x: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOX_COLUMN_Y)
    var y: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOX_COLUMN_WIDTH)
    var width: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOX_COLUMN_HEIGHT)
    var height: Int,

    @ColumnInfo(name = MusicScoreBooksContract.TABLE_BOX_COLUMN_PAGE_ID)
    val pageId: Int
)

fun BoxItem.toDatabase(pageId: Int) = BoxEntity(id, x, y, width, height, pageId)