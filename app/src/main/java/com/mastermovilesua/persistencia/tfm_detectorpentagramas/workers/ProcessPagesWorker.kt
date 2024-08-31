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
class ProcessPagesWorker @AssistedInject constructor(
    private val pageRepository: PageRepository,
    private val boxRepository: BoxRepository,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val bookDataset = inputData.getInt(Keys.BOOK_DATASET_KEY, -1)
        val pagesId = inputData.getIntArray(Keys.PAGE_ID_KEY)

        Log.d("ProcessPagesWorker", "Starting work!")

        if (bookDataset == -1 || pagesId == null) {
            Log.d("ProcessPagesWorker", "Failed: dataset $bookDataset and pagesId ${pagesId}!")
            return Result.failure()
        }

        try {
            pagesId.forEach { pageId ->
                pageRepository.getPage(pageId)?.let { pageToProcess ->
                    pageRepository.updatePages(listOf(pageToProcess.apply {
                        processState = PageState.Processing
                    }))
                } ?: return Result.failure()
            }

            pagesId.forEachIndexed { index, pageId ->
                setForeground(createForegroundInfo(index, pagesId.size))

                val processResult = pageRepository.getProcessedPageBoxes(bookDataset, pageId)
                Log.d("ProcessPagesWorker", "Got response from server: $processResult")

                processResult?.let { boxes ->
                    boxRepository.deleteBoxesFromPage(pageId)

                    boxes.map { box -> boxRepository.insertBox(pageId, box) }
                    Log.d("ProcessPagesWorker", "SUCCESS!")

                    pageRepository.getPage(pageId)?.let { pageToProcess ->
                        pageRepository.updatePages(listOf(pageToProcess.apply {
                            processState = PageState.Processed
                        }))
                    } ?: return Result.failure()
                } ?: run {
                    pageRepository.getPage(pageId)?.let { pageToProcess ->
                        pageRepository.updatePages(listOf(pageToProcess.apply {
                            processState = PageState.FailedToProcess
                        }))
                    } ?: return Result.failure()
                }
            }
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.d("ProcessPagesWorker", "UnknownHostException: ${e}! Retrying...")
                return Result.retry()
            } else {
                Log.e("ProcessPagesWorker", "EXECPTION: ${e}! Retrying...")
                return Result.failure()
            }
        }

        return Result.success()
    }

    private fun createForegroundInfo(progress: Int, total: Int): ForegroundInfo {
        val notification = Notifications.createProgressNotification(
            applicationContext,
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NOTIFICATION_TITLE,
            "Server is processing page $progress of ${total}."
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification
            )
        }
    }

    @AssistedFactory
    interface ProcessPagesWorkerFactory {
        fun create(context: Context, workerParameters: WorkerParameters): ProcessPagesWorker
    }

    companion object Keys {
        const val BOOK_DATASET_KEY = "bookDataset"
        const val PAGE_ID_KEY = "pageId"

        const val NOTIFICATION_ID = 2
        const val NOTIFICATION_CHANNEL_ID = "process_pages_id"
        const val NOTIFICATION_CHANNEL_NAME = "ProcessPages"

        const val NOTIFICATION_TITLE = "Processing Pages"
        const val NOTIFICATION_TEXT = "A page is currently being processed by the server..."
    }
}