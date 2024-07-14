package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookWithPagesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagesListViewModel @Inject constructor(
    private val getBookWithPagesUseCase: GetBookWithPagesUseCase,
    private val insertPageUseCase: InsertPageUseCase
) : ViewModel() {

    private val _bookModel = MutableLiveData<BookItem>()
    private val _pagesModel = MutableLiveData<List<PageItem>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val bookModel : LiveData<BookItem> get() = _bookModel
    val pagesModel : LiveData<List<PageItem>> get() = _pagesModel
    val isLoading : LiveData<Boolean> get() = _isLoading

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

    fun insertPage(page: PageItem) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            insertPageUseCase(bookId, page)

            _isLoading.postValue(false)
        }
    }
}