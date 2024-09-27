package com.mastermovilesua.persistencia.tfm_detectorpentagramas.workers

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.Notifications
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.ProcessPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ProcessResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.net.UnknownHostException

@HiltWorker
class ProcessPageWorker @AssistedInject constructor(
    private val processPageUseCase: ProcessPageUseCase,
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

            val processResults = processPageUseCase(pageId = pageId, bookDataset = bookDataset)

            if (processResults == ProcessResult.Failed) return Result.failure()

        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.d("ProcessPageWorker", "UnknownHostException: ${e}! Retrying...")
                return Result.retry()
            }

            Log.e("ProcessPageWorker", "UnknownHostException: ${e}! Retrying...")
            return Result.failure()
        }

        return Result.success()
    }

    private fun createForegroundInfo(pageId: Int): ForegroundInfo {
        val notification = Notifications.createNotification(
            applicationContext,
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            applicationContext.getString(R.string.notification_processing_page_title),
            applicationContext.getString(R.string.notification_processing_page_message)
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
    }
}

