package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetBookUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.RequestDownloadSamplePagesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.RequestProcessPagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPageViewModel @Inject constructor(
    private val insertPageUseCase: InsertPageUseCase,
    private val getBookUseCase: GetBookUseCase,
    private val requestProcessPagesUseCase: RequestProcessPagesUseCase,
    private val requestDownloadSamplePagesUseCase: RequestDownloadSamplePagesUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _pageInserted = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    val pageInserted: LiveData<Boolean> get() = _pageInserted

    private var bookId: Int = 0
    private var bookTitle: String = ""

    fun onCreate(bookId: Int) {
        this.bookId = bookId

        viewModelScope.launch {
            getBookUseCase(bookId)?.let { book -> bookTitle = book.title }
        }

        _pageInserted.postValue(false)
    }

    fun insertPages(selectedUris: List<Uri>) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            val pagesId = selectedUris.mapNotNull { selectedUri ->
                val selectedImage = SaveToMediaStore.loadImageAsByteArray(context, selectedUri)

                selectedImage?.let {
                    val insertedPageId = insertPageUseCase(
                        bookId,
                        selectedUri.lastPathSegment ?: bookTitle,
                        it
                    )

                    if (insertedPageId > 0) insertedPageId
                    else null
                }
            }

            getBookUseCase(bookId)?.let { book ->
                requestProcessPagesUseCase(book.dataset, pagesId.toTypedArray())
            }

            _isLoading.postValue(false)
            _pageInserted.postValue(true)
        }
    }

    fun downloadPictures() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _pageInserted.postValue(requestDownloadSamplePagesUseCase(bookId))

            _isLoading.postValue(false)
        }
    }
}