package com.mahmoudibrahem.mapsplayground.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var isKeepSplash =true
        private set

    init {
        viewModelScope.launch {
            delay(1000)
            isKeepSplash = false
        }
    }
}