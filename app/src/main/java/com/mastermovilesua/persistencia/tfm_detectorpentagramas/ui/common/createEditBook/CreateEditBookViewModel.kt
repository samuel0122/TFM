package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.createEditBook

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.UpdateBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.Dataset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditBookViewModel @Inject constructor(
    private val context: Context,
    private val getBookUseCase: GetBookUseCase,
    private val insertBookUseCase: InsertBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase
) : ViewModel() {

    private val _bookModel = MutableLiveData<BookItem>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _titleError = MutableLiveData<String?>()
    private val _descriptionError = MutableLiveData<String?>()
    private val _bookSubmited = MutableLiveData<Boolean>()

    val bookModel: LiveData<BookItem> get() = _bookModel
    val isLoading: LiveData<Boolean> get() = _isLoading
    val titleError: LiveData<String?> get() = _titleError
    val descriptionError: LiveData<String?> get() = _descriptionError
    val bookSubmited: LiveData<Boolean> get() = _bookSubmited

    private var isNewBook: Boolean = false
    private var bookId: Int = -1

    fun onCreate(bookId: Int, isEditing: Boolean) {
        this.bookId = bookId
        isNewBook = !isEditing

        viewModelScope.launch {
            _isLoading.postValue(true)

            if (isNewBook) {
                _bookModel.postValue(BookItem(title = "", description = ""))
            } else {
                getBookUseCase(bookId)?.let { book -> _bookModel.postValue(book) }
            }

            _isLoading.postValue(false)
        }
    }

    fun onSubmit(title: String, description: String, dataset: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            var hasError = false

            if (title.isEmpty()) {
                _titleError.postValue(context.getString(R.string.title_can_not_be_empty))
                hasError = true
            }
            if (description.isEmpty()) {
                _descriptionError.postValue(context.getString(R.string.description_can_not_be_empty))
                hasError = true
            }

            if (!hasError) {
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

                    _bookSubmited.postValue(true)
                }
            }

            _isLoading.postValue(false)
        }
    }

}