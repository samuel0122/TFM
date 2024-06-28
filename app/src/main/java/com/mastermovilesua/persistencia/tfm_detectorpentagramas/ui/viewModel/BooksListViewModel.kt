package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BooksRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.ImagenesRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBooksUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetImagenesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class BooksListViewModel @Inject constructor(
  private val getBooksUseCase: GetBooksUseCase
) : ViewModel() {
  val booksModel = MutableLiveData<List<BookItem>>()
  val isLoading = MutableLiveData<Boolean>()

  fun onCreate() {
    viewModelScope.launch {
      isLoading.postValue(true)

      booksModel.postValue(getBooksUseCase())

      isLoading.postValue(false)
    }
  }

}