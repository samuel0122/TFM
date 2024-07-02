package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetPageWithBoxesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageDetailViewModel @Inject constructor(
    private val getPageWithBoxesUseCase: GetPageWithBoxesUseCase
) : ViewModel() {

    val pageModel = MutableLiveData<PageItem>()
    val boxesModel = MutableLiveData<List<BoxItem>>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate(pageId: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)

            getPageWithBoxesUseCase(pageId)?.let { bookWithPagesModel ->
                pageModel.postValue(bookWithPagesModel.page)
                boxesModel.postValue(bookWithPagesModel.boxes)
            }

            isLoading.postValue(false)
        }
    }
}