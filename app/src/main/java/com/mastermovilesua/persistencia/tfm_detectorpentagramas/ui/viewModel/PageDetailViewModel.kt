package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.LiveData
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

    private val _pageModel = MutableLiveData<PageItem>()
    private val _boxesModel = MutableLiveData<List<BoxItem>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val pageModel: LiveData<PageItem> get() = _pageModel
    val boxesModel: LiveData<List<BoxItem>> get() = _boxesModel
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun onCreate(pageId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            getPageWithBoxesUseCase(pageId)?.let { bookWithPagesModel ->
                _pageModel.postValue(bookWithPagesModel.page)
                _boxesModel.postValue(bookWithPagesModel.boxes)
            }

            _isLoading.postValue(false)
        }
    }
}