package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.ProcessPagesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPageViewModel @Inject constructor(
    private val insertPageUseCase: InsertPageUseCase,
    private val getBookUseCase: GetBookUseCase,
    private val processPagesUseCase: ProcessPagesUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _pageInserted = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    val pageInserted: LiveData<Boolean> get() = _pageInserted

    private var bookId: Int = 0

    fun onCreate(bookId: Int) {
        this.bookId = bookId

        _pageInserted.postValue(false)
    }

    fun insertPages(pages: List<PageItem>) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            val pagesId = emptyList<Int>().toMutableList()

            pages.forEach { page ->
                val insertedPageId = insertPageUseCase(bookId, page)

                if (insertedPageId > 0) {
                    pagesId.add(insertedPageId)
                }
            }

            getBookUseCase(bookId)?.let { book ->
                processPagesUseCase(book.dataset, pagesId.toTypedArray())
            }

            _isLoading.postValue(false)
            _pageInserted.postValue(true)
        }
    }
}