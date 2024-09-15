package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookWithPagesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagesPagerViewModel @Inject constructor(
    private val getBookWithPagesUseCase: GetBookWithPagesUseCase
) : ViewModel() {
    private val _pages = MutableLiveData<List<ID>>()
    private val _isEditMode = MutableLiveData<Boolean>()

    val pages: LiveData<List<ID>> = _pages
    val isEditMode: LiveData<Boolean> get() = _isEditMode

    private var bookId: ID = -1


    fun onCreate(bookId: ID) {
        this.bookId = bookId
        viewModelScope.launch {
            getBookWithPagesUseCase(bookId).collect { bookWithPages ->
                _pages.postValue(bookWithPages.pages.map { it.id })
            }

            _isEditMode.postValue(false)
        }
    }

    fun setEditMode(isEditMode: Boolean) {
        _isEditMode.postValue(isEditMode)
    }
}