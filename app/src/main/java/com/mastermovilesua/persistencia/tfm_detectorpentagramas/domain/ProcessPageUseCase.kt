package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.workers.ProcessPageWorker
import javax.inject.Inject

class ProcessPageUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val pageRepository: PageRepository,
    private val workManager: WorkManager
) {
    suspend operator fun invoke(pageId: Int): Boolean {
        val page = pageRepository.getPage(pageId) ?: return false
        if (page.processState == PageState.Processing) return false

        pageRepository.updatePage(page.apply { processState = PageState.Processing })

        bookRepository.getBookOfPage(pageId)?.let { book ->
            val workRequestBuilder = OneTimeWorkRequestBuilder<ProcessPageWorker>()

            workRequestBuilder
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        ProcessPageWorker.Keys.BOOK_DATASET_KEY to book.dataset.value,
                        ProcessPageWorker.Keys.PAGE_ID_KEY to pageId
                    )
                )

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                workRequestBuilder.setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    duration = java.time.Duration.ofSeconds(10)
                )
            } else {
                workRequestBuilder
                    .setBackoffCriteria(
                        backoffPolicy = BackoffPolicy.LINEAR,
                        10, TimeUnit.SECONDS
                    )
            }
             */

            workManager.enqueueUniqueWork(
                "ProcessPageWorker",
                ExistingWorkPolicy.APPEND,
                workRequestBuilder.build()
            )
            return true
        }

        return false
    }
}