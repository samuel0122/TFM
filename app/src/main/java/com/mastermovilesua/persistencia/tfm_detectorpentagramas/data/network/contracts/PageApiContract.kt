package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts

object PageApiContract {
    const val API_BASE_URL = "https://tfm-detector-pentagramas-api-a15aebd93b08.herokuapp.com/"

    const val BOOK_FIELD_DATASET = "dataset"

    const val PAGE_FIELD_ID = "pageId"
    const val PAGE_FIELD_IMAGE = "page"
    const val PAGE_FIELD_BOXES = "boxes"

    const val BOX_FIELD_X = "x"
    const val BOX_FIELD_Y = "y"
    const val BOX_FIELD_WIDTH = "w"
    const val BOX_FIELD_HEIGHT = "h"

    const val API_VERSION = "/api/v1/"
    object GETRequests {
       const val STATUS = "${API_VERSION}status"

        const val DOWNLOADABLE_IMAGES_DATASET_PATH = "dataset"
        const val DOWNLOADABLE_IMAGES = "${API_VERSION}images/{${DOWNLOADABLE_IMAGES_DATASET_PATH}}"

        const val DOWNLOAD_IMAGE_DATASET_PATH = "dataset"
        const val DOWNLOAD_IMAGE_FILE_PATH = "file"
        const val DOWNLOAD_IMAGE = "${API_VERSION}image/{${DOWNLOAD_IMAGE_DATASET_PATH}}/{${DOWNLOAD_IMAGE_FILE_PATH}}"
    }
    object POSTRequests {
        const val PROCESS_IMAGE = "${API_VERSION}process-image"
    }
}