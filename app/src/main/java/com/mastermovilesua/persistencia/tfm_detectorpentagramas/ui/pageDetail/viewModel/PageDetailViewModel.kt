package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DeleteBoxUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetPageWithBoxesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertBoxUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.UpdateBoxUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ID
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageDetailViewModel @Inject constructor(
    private val getPageWithBoxesUseCase: GetPageWithBoxesUseCase,
    private val insertBoxUseCase: InsertBoxUseCase,
    private val deleteBoxUseCase: DeleteBoxUseCase,
    private val updateBoxUseCase: UpdateBoxUseCase
) : ViewModel() {

    private val _pageModel = MutableLiveData<PageItem>()
    private val _boxesModel = MutableLiveData<List<BoxItem>>()
    private val _selectedBox = MutableLiveData<BoxItem?>()
    private val _isLoading = MutableLiveData<Boolean>()

    val pageModel: LiveData<PageItem> get() = _pageModel
    val boxesModel: LiveData<List<BoxItem>> get() = _boxesModel
    val selectedBox: LiveData<BoxItem?> get() = _selectedBox
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var pageId: ID = -1

    fun onCreate(pageId: ID) {
        this.pageId = pageId
        viewModelScope.launch {
            _isLoading.postValue(true)

            getPageWithBoxesUseCase(pageId).collect { bookWithPagesModel ->
                _pageModel.postValue(bookWithPagesModel.page)
                _boxesModel.postValue(bookWithPagesModel.boxes)

            }

            _isLoading.postValue(false)
        }
    }

    fun insertBox(box: BoxItem) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            insertBoxUseCase(pageId, box)

            _isLoading.postValue(false)
        }
    }

    fun updateBox(box: BoxItem) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            updateBoxUseCase(box)

            _isLoading.postValue(false)
        }
    }

    fun deleteBox(box: BoxItem) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            deleteBoxUseCase(box.id)

            _isLoading.postValue(false)
        }
    }

    fun selectBox(box: BoxItem) {
        _selectedBox.postValue(box)
    }

    fun deselectBox() {
        _selectedBox.postValue(null)
    }
}