package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts

object MusicScoreBooksContract {
    // Constantes para la base de datos
    const val DATABASE_NAME = "musicScoreBooks.db"
    const val DATABASE_VERSION = 1

    // Constantes para la tabla Book
    const val TABLE_BOOK = "Book"
    const val TABLE_BOOK_COLUMN_BOOK_ID = "bookId"
    const val TABLE_BOOK_COLUMN_TITLE = "title"
    const val TABLE_BOOK_COLUMN_DESCRIPTION = "description"
    const val TABLE_BOOK_COLUMN_DATASET = "dataset"

    // Constantes para la tabla Page
    const val TABLE_PAGE = "Page"
    const val TABLE_PAGE_COLUMN_PAGE_ID = "pageId"
    const val TABLE_PAGE_COLUMN_IMAGE_URI = "imageUri"
    const val TABLE_PAGE_COLUMN_PROCESSED = "processed"
    const val TABLE_PAGE_COLUMN_ORDER = "order"
    const val TABLE_PAGE_COLUMN_BOOK_ID = "bookId"

    // Constantes para la tabla Box
    const val TABLE_BOX = "Box"
    const val TABLE_BOX_COLUMN_BOX_ID = "boxId"
    const val TABLE_BOX_COLUMN_X = "x"
    const val TABLE_BOX_COLUMN_Y = "y"
    const val TABLE_BOX_COLUMN_WIDTH = "width"
    const val TABLE_BOX_COLUMN_HEIGHT = "height"
    const val TABLE_BOX_COLUMN_PAGE_ID = "pageId"

    const val AUTHORITY = "com.mastermovilesua.persistencia.tfm_detectorpentagramas"
}