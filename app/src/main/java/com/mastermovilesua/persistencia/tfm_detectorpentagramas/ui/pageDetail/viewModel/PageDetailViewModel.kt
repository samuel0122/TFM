package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DeleteBoxUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DeletePageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBoxesForPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetPageWithBoxesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertBoxUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.UpdateBoxUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ID
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class PageDetailViewModel @Inject constructor(
    private val getPageWithBoxesUseCase: GetPageWithBoxesUseCase,
    private val getPageUseCase: GetPageUseCase,
    private val getBoxesForPageUseCase: GetBoxesForPageUseCase,
    private val deletePageUseCase: DeletePageUseCase,
    private val insertBoxUseCase: InsertBoxUseCase,
    private val deleteBoxUseCase: DeleteBoxUseCase,
    private val updateBoxUseCase: UpdateBoxUseCase
) : ViewModel() {

    private val _pageModel = MutableLiveData<PageItem>()
    private val _boxesModel = MutableLiveData<List<BoxItem>>()
    private val _selectedBoxId = MutableLiveData<Int?>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isEditMode = MutableLiveData<Boolean>()

    val pageModel: LiveData<PageItem> get() = _pageModel
    val boxesModel: LiveData<List<BoxItem>> get() = _boxesModel
    val selectedBoxId: LiveData<Int?> get() = _selectedBoxId
    val isLoading: LiveData<Boolean> get() = _isLoading
    val isEditMode: LiveData<Boolean> get() = _isEditMode

    private var pageId: ID = -1

    fun onCreate(pageId: ID) {
        this.pageId = pageId
        viewModelScope.launch {
            _isLoading.postValue(true)

            getPageUseCase(pageId)?.let { pageModel ->
                _pageModel.postValue(pageModel)
            }

            getBoxesForPageUseCase(pageId).collect { boxesModel ->
                Log.e("PageDetailViewModel", "COLLECTING BOXES")
                _boxesModel.postValue(boxesModel)
            }

            _isLoading.postValue(false)
        }
    }

    fun insertNewBox() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            val box = boxesModel.value?.takeIf { it.isNotEmpty() }?.let { boxes ->
                val totalX = boxes.sumOf { it.x.toDouble() }.toFloat()
                val totalY = boxes.sumOf { it.y.toDouble() }.toFloat()
                val totalWith = boxes.sumOf { it.width.toDouble() }.toFloat()
                val totalHeight = boxes.sumOf { it.height.toDouble() }.toFloat()
                val numBoxes = boxes.size
                BoxItem(
                    id = 0,
                    x = totalX / numBoxes,
                    y = totalY / numBoxes,
                    width = totalWith / numBoxes,
                    height = totalHeight / numBoxes
                )
            } ?: BoxItem(0, 0f, 0f, 0.3f, 0.1f)

            insertBoxUseCase(pageId, box)

            _isLoading.postValue(false)
        }
    }

    fun updateBox(box: BoxItem) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            box.apply {
                width = max(width, minBoxWidth)
                height = max(height, minBoxHeight)
            }

            updateBoxUseCase(box)

            _isLoading.postValue(false)
        }
    }

    fun deleteBox(boxId: ID) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            deleteBoxUseCase(boxId)

            _isLoading.postValue(false)
        }
    }

    fun selectBox(boxId: ID?) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _selectedBoxId.postValue(boxId)

            _isLoading.postValue(false)
        }
    }

    fun enableEditMode() {
        _isEditMode.postValue(true)
    }

    fun disableEditMode() {
        _isEditMode.postValue(false)
        _selectedBoxId.postValue(null)
    }

    fun deletePage() {
        viewModelScope.launch {
            deletePageUseCase(pageId);
        }
    }

    companion object {
        private const val minBoxWidth = 0.15f
        private const val minBoxHeight = 0.05f
    }
}