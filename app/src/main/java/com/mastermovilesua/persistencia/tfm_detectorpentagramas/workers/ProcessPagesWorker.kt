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
class ProcessPagesWorker @AssistedInject constructor(
    private val processPageUseCase: ProcessPageUseCase,
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
            pagesId.forEachIndexed { index, pageId ->
                setForeground(createForegroundInfo(index, pagesId.size))

                val processResults = processPageUseCase(pageId = pageId, bookDataset = bookDataset)

                if (processResults == ProcessResult.Failed) return Result.failure()
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
            applicationContext.getString(R.string.notification_processing_pages_title),
            applicationContext.getString(R.string.server_is_processing_page_X_of_Y, progress, total)
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
    }
}