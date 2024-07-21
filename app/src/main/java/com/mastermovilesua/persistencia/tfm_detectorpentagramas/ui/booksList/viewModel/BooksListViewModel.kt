package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.booksList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.DeleteBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBooksUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksListViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    private val _booksModel = MutableLiveData<List<BookItem>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isEditMode = MutableLiveData<Boolean>()
    private val _isAllSelected = MutableLiveData<Boolean>()
    private val _selectedBooksIds = MutableLiveData<Set<Int>>()

    val booksModel: LiveData<List<BookItem>> get() = _booksModel
    val isLoading: LiveData<Boolean> get() = _isLoading
    val isEditMode: LiveData<Boolean> get() = _isEditMode
    val isAllSelected: LiveData<Boolean> get() = _isAllSelected
    val selectedBooksIds: LiveData<Set<Int>> get() = _selectedBooksIds

    fun onCreate() {
        viewModelScope.launch {
            _isEditMode.postValue(false)
            _isAllSelected.postValue(false)
            _selectedBooksIds.postValue(emptySet())

            _isLoading.postValue(true)

            getBooksUseCase().collect { booksList ->
                _booksModel.postValue(booksList)
            }

            _isLoading.postValue(false)
        }
    }

    fun selectBook(bookId: Int) {
        if (selectedBooksIds.value?.contains(bookId) == true) {
            val newSelectedBooks = selectedBooksIds.value?.minus(bookId) ?: emptySet()
            _selectedBooksIds.postValue(newSelectedBooks)
            if (_isAllSelected.value == true && newSelectedBooks.size != booksModel.value?.size) {
                _isAllSelected.postValue(false)
            }
        } else {
            val newSelectedBooks = selectedBooksIds.value?.plus(bookId) ?: setOf(bookId)
            _selectedBooksIds.postValue(newSelectedBooks)
            if (newSelectedBooks.size == booksModel.value?.size) {
                _isAllSelected.postValue(true)
            }
        }
    }

    fun enableEditMode() {
        _isEditMode.postValue(true)
    }

    fun disableEditMode() {
        _selectedBooksIds.postValue(emptySet())
        _isEditMode.postValue(false)
    }

    fun toggleAllSelection() {
        if (selectedBooksIds.value?.size == booksModel.value?.size) {
            _selectedBooksIds.postValue(emptySet())
            _isAllSelected.postValue(false)
        } else {
            _selectedBooksIds.postValue(booksModel.value?.map { it.id }?.toSet())
            _isAllSelected.postValue(true)
        }
    }

    fun deleteBooks() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            selectedBooksIds.value?.forEach { deleteBookUseCase(it) }

            _isLoading.postValue(false)

            _selectedBooksIds.postValue(emptySet())
            _isEditMode.postValue(false)
        }
    }
}