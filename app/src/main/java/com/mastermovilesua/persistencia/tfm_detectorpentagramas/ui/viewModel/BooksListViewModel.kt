package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBooksUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksListViewModel @Inject constructor(
  private val getBooksUseCase: GetBooksUseCase
) : ViewModel() {

  val booksModel = MutableLiveData<List<BookItem>>()
  val isLoading = MutableLiveData<Boolean>()

  fun onCreate() {
    viewModelScope.launch {
      isLoading.postValue(true)

      getBooksUseCase().collect {
        booksModel.postValue(it)
      }

      isLoading.postValue(false)
    }
  }

}