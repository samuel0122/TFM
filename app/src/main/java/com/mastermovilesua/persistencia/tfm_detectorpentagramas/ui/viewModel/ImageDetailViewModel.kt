package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetImagenUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val getImagenUseCase: GetImagenUseCase
) : ViewModel() {
    val imageModel = MutableLiveData<ImagenesItem>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate(imageId: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)

            imageModel.postValue(getImagenUseCase(imageId))

            isLoading.postValue(false)
        }
    }
}