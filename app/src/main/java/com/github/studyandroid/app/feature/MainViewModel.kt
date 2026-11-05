package com.github.studyandroid.app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _effect = Channel<MainEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.ExitApp -> viewModelScope.launch {
                _effect.send(MainEffect.ExitApp)
            }
        }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }
}
