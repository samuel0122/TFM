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

        pageRepository.updatePage(page.apply { processState = PageState.Processing })

        val processResult = pageRepository.getProcessedPageBoxes(bookDataset, pageId)

        if (processResult != null) {
            boxRepository.deleteBoxesFromPage(pageId)

            processResult.map { box -> boxRepository.insertBox(pageId, box) }

            val pageProcessed = pageRepository.getPage(pageId) ?: return ProcessResult.Failed
            pageRepository.updatePage(pageProcessed.apply { processState = PageState.Processed })
        } else {
            val pageFailed = pageRepository.getPage(pageId) ?: return ProcessResult.Failed

            pageRepository.updatePage(pageFailed.apply { processState = PageState.FailedToProcess })
        }

        return ProcessResult.Success
    }
}