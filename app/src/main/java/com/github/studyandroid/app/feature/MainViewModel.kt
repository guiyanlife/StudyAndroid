package com.github.studyandroid.app.feature

import com.github.studyandroid.app.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() :
    BaseViewModel<MainUiState, MainIntent, MainEffect>(MainUiState) {

    override fun handleIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.ExitApp -> sendEffect(MainEffect.ExitApp)
        }
    }
}
