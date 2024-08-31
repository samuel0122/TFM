package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.workers.ProcessPageWorker
import java.time.Duration
import javax.inject.Inject

class ProcessPageUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val workManager: WorkManager
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(pageId: Int): Boolean {

        bookRepository.getBookOfPage(pageId)?.let { book ->
            val workRequest = OneTimeWorkRequestBuilder<ProcessPageWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        ProcessPageWorker.Keys.BOOK_DATASET_KEY to book.dataset.value,
                        ProcessPageWorker.Keys.PAGE_ID_KEY to pageId
                    )
                )
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    duration = Duration.ofSeconds(10)
                )
                .build()

            workManager.enqueue(workRequest)
            return true
        }

        return false
    }
}