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
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DownloadAndSaveSamplePagesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.net.UnknownHostException

@HiltWorker
class DownloadSamplePagesWorker @AssistedInject constructor(
    private val downloadAndSaveSamplePagesUseCase: DownloadAndSaveSamplePagesUseCase,
    private val getBookUseCase: GetBookUseCase,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val bookId = inputData.getInt(Keys.BOOK_ID, -1)

        Log.d(TAG, "Starting work!")

        if (bookId == -1) {
            Log.d(TAG, "Failed: book ID $bookId!")
            return Result.failure()
        }

        val book = getBookUseCase(bookId) ?: return Result.failure()
        setForeground(createForegroundInfo(book.title))
        try {
            downloadAndSaveSamplePagesUseCase(bookId)
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.d(TAG, "UnknownHostException: ${e.message}! Retrying...")
                return Result.retry()
            } else {
                Log.e(TAG, "Exception: ${e.message}! Retrying...")
                return Result.failure()
            }
        }

        return Result.success()
    }

    private fun createForegroundInfo(bookTitle: String): ForegroundInfo {
        val notification = Notifications.createNotification(
            applicationContext,
            Keys.NOTIFICATION_CHANNEL_ID,
            Keys.NOTIFICATION_CHANNEL_NAME,
            applicationContext.getString(R.string.notification_downloading_sample_pages_title),
            applicationContext.getString(R.string.downloading_sample_page_for_book_X, bookTitle)
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                Keys.NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                Keys.NOTIFICATION_ID,
                notification
            )
        }
    }

    @AssistedFactory
    interface DownloadSamplePagesWorkerFactory {
        fun create(context: Context, workerParameters: WorkerParameters): DownloadSamplePagesWorker
    }

    private val TAG = "DownloadSamplePagesWorker"

    companion object Keys {
        const val BOOK_ID = "bookId"

        private const val NOTIFICATION_ID = 3
        private const val NOTIFICATION_CHANNEL_ID = "download_sample_pages_id"
        private const val NOTIFICATION_CHANNEL_NAME = "DownloadSamplePages"
    }
}