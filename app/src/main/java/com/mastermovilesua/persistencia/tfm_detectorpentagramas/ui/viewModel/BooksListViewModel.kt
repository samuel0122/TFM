package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
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

  private val _booksModel = MutableLiveData<List<BookItem>>()
  private val _isLoading = MutableLiveData<Boolean>()

  val booksModel: LiveData<List<BookItem>> get() = _booksModel
  val isLoading: LiveData<Boolean> get() = _isLoading

  fun onCreate() {
    viewModelScope.launch {
      _isLoading.postValue(true)

      getBooksUseCase().collect { booksList ->
        _booksModel.postValue(booksList)
      }

      _isLoading.postValue(false)
    }
  }

}