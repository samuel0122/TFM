package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DeleteBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DeletePageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookWithPagesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.UpdatePageOrderUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookWithPagesUseCase: GetBookWithPagesUseCase,
    private val deletePageUseCase: DeletePageUseCase,
    private val deleteBookUseCase: DeleteBookUseCase,
    private val updatePageOrderUseCase: UpdatePageOrderUseCase
) : ViewModel() {

    private val _bookModel = MutableLiveData<BookItem>()
    private val _pagesModel = MutableLiveData<List<PageItem>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isEditMode = MutableLiveData<Boolean>()
    private val _isAllSelected = MutableLiveData<Boolean>()
    private val _selectedPagesIds = MutableLiveData<Set<Int>>()
    private val _bookDeleted = MutableLiveData<Boolean>()

    val bookModel: LiveData<BookItem> get() = _bookModel
    val pagesModel: LiveData<List<PageItem>> get() = _pagesModel
    val isLoading: LiveData<Boolean> get() = _isLoading
    val isEditMode: LiveData<Boolean> get() = _isEditMode
    val isAllSelected: LiveData<Boolean> get() = _isAllSelected
    val selectedPagesIds: LiveData<Set<Int>> get() = _selectedPagesIds
    val bookDeleted: LiveData<Boolean> get() = _bookDeleted

    private var bookId: Int = 0

    fun onCreate(bookId: Int) {
        this.bookId = bookId
        viewModelScope.launch {
            _isLoading.postValue(true)

            getBookWithPagesUseCase(bookId).collect { bookWithPagesModel ->
                _bookModel.postValue(bookWithPagesModel.book)
                _pagesModel.postValue(bookWithPagesModel.pages)
            }

            _isLoading.postValue(false)
        }
    }

    fun selectPage(pageId: Int) {
        if (selectedPagesIds.value?.contains(pageId) == true) {
            val newSelectedPages = selectedPagesIds.value?.minus(pageId) ?: emptySet()
            _selectedPagesIds.postValue(newSelectedPages)
            if (_isAllSelected.value == true && newSelectedPages.size != pagesModel.value?.size) {
                _isAllSelected.postValue(false)
            }
        } else {
            val newSelectedPages = selectedPagesIds.value?.plus(pageId) ?: setOf(pageId)
            _selectedPagesIds.postValue(newSelectedPages)
            if (newSelectedPages.size == pagesModel.value?.size) {
                _isAllSelected.postValue(true)
            }
        }
    }

    fun enableEditMode() {
        _isEditMode.postValue(true)
    }

    fun disableEditMode() {
        _selectedPagesIds.postValue(emptySet())
        _isAllSelected.postValue(false)
        _isEditMode.postValue(false)
    }

    fun toggleAllSelection() {
        if (selectedPagesIds.value?.size == pagesModel.value?.size) {
            _selectedPagesIds.postValue(emptySet())
            _isAllSelected.postValue(false)
        } else {
            _selectedPagesIds.postValue(pagesModel.value?.map { it.id }?.toSet())
            _isAllSelected.postValue(true)
        }
    }

    fun deletePages() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            selectedPagesIds.value?.forEach { deletePageUseCase(it) }

            _selectedPagesIds.postValue(emptySet())
            _isAllSelected.postValue(false)
            _isEditMode.postValue(false)

            _isLoading.postValue(false)
        }
    }

    fun deleteBook() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            if (deleteBookUseCase(bookId)) _bookDeleted.postValue(true)

            _isLoading.postValue(false)
        }
    }

    fun changePageOrder(pageId: Int, newOrder: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            updatePageOrderUseCase(pageId, newOrder)

            _isLoading.postValue(false)
        }
    }
}