package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

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

    val bookModel = MutableLiveData<BookItem>()
    val pagesModel = MutableLiveData<List<PageItem>>()
    val isLoading = MutableLiveData<Boolean>()

    private var bookId: Int = 0

    fun onCreate(bookId: Int) {
        this.bookId = bookId
        viewModelScope.launch {
            isLoading.postValue(true)

            getBookWithPagesUseCase(bookId)?.let { bookWithPagesModel ->
                bookModel.postValue(bookWithPagesModel.book)
                pagesModel.postValue(bookWithPagesModel.pages)
            }

            isLoading.postValue(false)
        }
    }

    fun insertPage(page: PageItem) {
        viewModelScope.launch {
            isLoading.postValue(true)

            insertPageUseCase(bookId, page)

            getBookWithPagesUseCase(bookId)?.let { bookWithPagesModel ->
                bookModel.postValue(bookWithPagesModel.book)
                pagesModel.postValue(bookWithPagesModel.pages)
            }

            isLoading.postValue(false)
        }
    }
}