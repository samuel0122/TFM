package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBooksUseCase
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

    val isLoading : LiveData<Boolean> get() = _isLoading

    private var bookId: Int = 0

    fun onCreate(bookId: Int) {
        this.bookId = bookId
    }

    fun insertPage(page: PageItem) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            insertPageUseCase(bookId, page)

            _isLoading.postValue(false)
        }
    }
}