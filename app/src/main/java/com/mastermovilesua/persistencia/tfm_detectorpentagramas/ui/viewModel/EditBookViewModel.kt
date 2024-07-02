package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.UpdateBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val insertBookUseCase: InsertBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase
) : ViewModel() {

    val bookModel = MutableLiveData<BookItem>()
    val isLoading = MutableLiveData<Boolean>()

    private var isNewBook: Boolean = false

    fun onCreate(bookId: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)

            val getBook = getBookUseCase(bookId)

            if (getBook != null) {
                bookModel.postValue(getBook)
            } else {
                isNewBook = true
                bookModel.postValue(BookItem(title = "", description = ""))
            }

            isLoading.postValue(false)
        }
    }

    fun onSubmit(title: String, description: String) {
        viewModelScope.launch {
            isLoading.postValue(true)

            bookModel.value?.let { bookItem ->

                bookItem.apply {
                    this.title = title
                    this.description = description
                }

                if (isNewBook) {
                    insertBookUseCase(bookItem)
                } else {
                    updateBookUseCase(bookItem)
                }
            }

            isLoading.postValue(false)
        }
    }

}