package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.Dataset
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.workers.ProcessPagesWorker
import javax.inject.Inject

class RequestProcessPagesUseCase @Inject constructor(
    private val pageRepository: PageRepository,
    private val workManager: WorkManager
) {
    suspend operator fun invoke(pagesDataset: Dataset, pagesId: Array<Int>): Boolean {

        pagesId.forEach { pageId ->
            pageRepository.getPage(pageId)?.let { page ->
                pageRepository.updatePage(page.apply {
                    processState = PageState.WaitingForProcessing
                })
            }
        }

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workInputData = workDataOf(
            ProcessPagesWorker.Keys.BOOK_DATASET_KEY to pagesDataset.value,
            ProcessPagesWorker.Keys.PAGE_ID_KEY to pagesId
        )

        val workRequest = OneTimeWorkRequestBuilder<ProcessPagesWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(workConstraints)
            .setInputData(workInputData)
            .build()

        workManager.enqueueUniqueWork(
            "ProcessPagesWorker",
            ExistingWorkPolicy.APPEND,
            workRequest
        )

        return true
    }
}