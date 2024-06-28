package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.GetImagenesUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertImagenUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getImagenesUseCase: GetImagenesUseCase,
    private val insertImagenUseCase: InsertImagenUseCase
) : ViewModel() {

    val imagenesModel = MutableLiveData<List<ImagenesItem>>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)

            imagenesModel.postValue(getImagenesUseCase())

            isLoading.postValue(false)
        }
    }

    fun insertImagen(imagen: ImagenesItem) {
        viewModelScope.launch {
            isLoading.postValue(true)

            insertImagenUseCase(imagen)
            imagenesModel.postValue(getImagenesUseCase())

            isLoading.postValue(false)
        }
    }
}