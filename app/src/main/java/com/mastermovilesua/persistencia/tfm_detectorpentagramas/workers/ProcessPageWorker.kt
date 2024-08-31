package com.mastermovilesua.persistencia.tfm_detectorpentagramas.workers

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.Notifications
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.net.UnknownHostException

@HiltWorker
class ProcessPageWorker @AssistedInject constructor(
    private val pageRepository: PageRepository,
    private val boxRepository: BoxRepository,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val bookDataset = inputData.getInt(Keys.BOOK_DATASET_KEY, -1)
        val pageId = inputData.getInt(Keys.PAGE_ID_KEY, -1)

        Log.d("ProcessPageWorker", "Starting work!")

        if (bookDataset == -1 || pageId == -1) {
            Log.d("ProcessPageWorker", "Failed: dataset $bookDataset and pageId ${pageId}!")
            return Result.failure()
        }

        try {
            setForeground(createForegroundInfo(pageId))

            pageRepository.getPage(pageId)?.let { pageToProcess ->
                pageRepository.updatePages(listOf(pageToProcess.apply {
                    processState = PageState.Processing
                }))
            } ?: return Result.failure()


            val processResult = pageRepository.getProcessedPageBoxes(bookDataset, pageId)
            Log.d("ProcessPageWorker", "Got response from server: $processResult")
            processResult?.let { boxes ->
                boxRepository.deleteBoxesFromPage(pageId)

                boxes.map { box -> boxRepository.insertBox(pageId, box) }
                Log.d("ProcessPageWorker", "SUCCESS!")

                pageRepository.getPage(pageId)?.let { pageToProcess ->
                    pageRepository.updatePages(listOf(pageToProcess.apply {
                        processState = PageState.Processed
                    }))
                } ?: return Result.failure()

                return Result.success()
            }

            pageRepository.getPage(pageId)?.let { pageToProcess ->
                pageRepository.updatePages(listOf(pageToProcess.apply {
                    processState = PageState.FailedToProcess
                }))
            } ?: return Result.failure()

            return Result.retry()
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.d("ProcessPageWorker", "UnknownHostException: ${e}! Retrying...")
                return Result.retry()
            }

            Log.e("ProcessPageWorker", "UnknownHostException: ${e}! Retrying...")
        }

        Log.d("ProcessPageWorker", "Failure at the end!")
        return Result.failure()
    }

    private fun createForegroundInfo(pageId: Int): ForegroundInfo {
        val notification = Notifications.createNotification(
            applicationContext,
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NOTIFICATION_TITLE,
            NOTIFICATION_TEXT
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                pageId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                pageId,
                notification
            )
        }
    }

    @AssistedFactory
    interface ProcessPageWorkerFactory {
        fun create(context: Context, workerParameters: WorkerParameters): ProcessPageWorker
    }

    companion object Keys {
        const val BOOK_DATASET_KEY = "bookDataset"
        const val PAGE_ID_KEY = "pageId"

        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "process_page_id"
        const val NOTIFICATION_CHANNEL_NAME = "ProcessPage"

        const val NOTIFICATION_TITLE = "Processing Page"
        const val NOTIFICATION_TEXT = "A page is currently being processed by the server..."
    }
}

