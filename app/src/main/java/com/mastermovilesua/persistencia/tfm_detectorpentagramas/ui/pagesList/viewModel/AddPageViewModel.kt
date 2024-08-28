package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPageViewModel @Inject constructor(
    private val insertPageUseCase: InsertPageUseCase
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

            pages.forEach { page ->
                insertPageUseCase(bookId, page)
            }

            _isLoading.postValue(false)
            _pageInserted.postValue(true)
        }
    }
}