package com.github.studyandroid.app.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.github.studyandroid.app.core.util.LogUtil
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * ViewModel公共基类，统一管理状态、事件和调试日志
 */
abstract class BaseViewModel<S : BaseUiState, I : BaseIntent, E : BaseEffect>(initialState: S) :
    ViewModel() {
    private val tag = javaClass.simpleName

    // State
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // State更新锁
    private val stateMutex = Mutex()

    // Effect
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        LogUtil.d(tag, "[Lifecycle] init")
        LogUtil.d(tag, "[State][Init] $initialState")
    }

    /**
     * UI 统一 Intent 入口
     */
    fun onIntent(intent: I) {
        LogUtil.d(tag, "[Intent] $intent")
        handleIntent(intent)
    }

    /**
     * 子类实现 Intent 处理
     */
    protected abstract fun handleIntent(intent: I)

    /**
     * 原子更新 State
     *
     * - Mutex 保证更新串行化
     * - 每次成功更新都打印 Old / New
     * - 不会因为 StateFlow 合并而遗漏日志
     */
    protected suspend fun updateState(reducer: S.() -> S) {
        stateMutex.withLock {
            val oldState = _uiState.value
            val newState = oldState.reducer()
            if (oldState != newState) {
                _uiState.value = newState
                LogUtil.d(tag, "[State][Old] $oldState")
                LogUtil.d(tag, "[State][New] $newState")
            }
        }
    }

    /**
     * 发送一次性 Effect
     */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
            LogUtil.d(tag, "[Effect] $effect")
        }
    }

    override fun onCleared() {
        LogUtil.d(tag, "[Lifecycle] onCleared")
        _effect.close()
    }
}