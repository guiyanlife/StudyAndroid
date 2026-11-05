package com.github.studyandroid.app.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.github.studyandroid.app.navigation.AppNavHost
import com.github.studyandroid.app.core.ui.preview.AppPreview
import com.github.studyandroid.app.core.ui.theme.AppTheme

@Composable
fun MainScreen(
    onExitApp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnExitApp = rememberUpdatedState(onExitApp)

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    MainEffect.ExitApp -> currentOnExitApp.value()
                }
            }
        }
    }

    MainContent(
        onIntent = viewModel::onIntent,
        modifier = modifier
    ) {
        AppNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun MainContent(
    onIntent: (MainIntent) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        FloatingActionButton(
            onClick = { onIntent(MainIntent.ExitApp) },
            shape = CircleShape,
            containerColor = Color.Red,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .safeDrawingPadding()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "退出应用"
            )
        }
    }
}

@AppPreview
@Composable
private fun MainPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainContent(onIntent = {}, content = {})
        }
    }
}
