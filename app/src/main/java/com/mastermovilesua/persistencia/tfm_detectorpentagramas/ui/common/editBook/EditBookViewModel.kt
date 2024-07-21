package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.editBook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.UpdateBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.Dataset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val insertBookUseCase: InsertBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase
) : ViewModel() {

    private val _bookModel = MutableLiveData<BookItem>()
    private val _isLoading = MutableLiveData<Boolean>()

    val bookModel: LiveData<BookItem> get() = _bookModel
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var isNewBook: Boolean = false

    fun onCreate(bookId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            val getBook = getBookUseCase(bookId)

            if (getBook != null) {
                _bookModel.postValue(getBook)
            } else {
                isNewBook = true
                _bookModel.postValue(BookItem(title = "", description = ""))
            }

            _isLoading.postValue(false)
        }
    }

    fun onSubmit(title: String, description: String, dataset: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            bookModel.value?.let { bookItem ->

                bookItem.apply {
                    this.title = title
                    this.description = description
                    this.dataset = Dataset.fromInt(dataset)
                }

                if (isNewBook) {
                    insertBookUseCase(bookItem)
                } else {
                    updateBookUseCase(bookItem)
                }
            }

            _isLoading.postValue(false)
        }
    }

}