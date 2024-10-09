package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ProcessResult
import javax.inject.Inject

class ProcessPageUseCase @Inject constructor(
    private val pageRepository: PageRepository,
    private val boxRepository: BoxRepository
) {
    suspend operator fun invoke(pageId: Int, bookDataset: Int): ProcessResult {
        val page = pageRepository.getPage(pageId) ?: return ProcessResult.Failed
        if (page.processState != PageState.WaitingForProcessing) return ProcessResult.Skipped

        updatePageState(pageId, PageState.Processing)?.let { return it }

        val processResult = try {
            pageRepository.getProcessedPageBoxes(bookDataset, pageId)
        } catch (e: Exception) {
            updatePageState(pageId, PageState.FailedToProcess)?.let { return it }
            throw e
        }

        if (processResult != null) {
            boxRepository.deleteBoxesFromPage(pageId)

            processResult.map { box -> boxRepository.insertBox(pageId, box) }

            updatePageState(pageId, PageState.Processed)?.let { return it }
        } else {
            updatePageState(pageId, PageState.FailedToProcess)?.let { return it }
        }

        return ProcessResult.Success
    }

    private suspend fun updatePageState(pageId: Int, pageState: PageState): ProcessResult? {
        val page = pageRepository.getPage(pageId) ?: return ProcessResult.Failed
        pageRepository.updatePage(page.apply { processState = pageState })
        return null
    }
}