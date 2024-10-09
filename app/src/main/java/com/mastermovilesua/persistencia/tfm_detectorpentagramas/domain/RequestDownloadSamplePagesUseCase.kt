package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.workers.DownloadSamplePagesWorker
import javax.inject.Inject

class RequestDownloadSamplePagesUseCase @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke(bookId: Int): Boolean {

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workInputData = workDataOf(
            DownloadSamplePagesWorker.Keys.BOOK_ID to bookId
        )

        val workRequest = OneTimeWorkRequestBuilder<DownloadSamplePagesWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(workConstraints)
            .setInputData(workInputData)
            .build()

        workManager.enqueueUniqueWork(
            "DownloadSamplePagesWorker",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest
        )

        return true
    }
}