package com.dx.star.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dx.star.model.remote.HttpResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 *
 * zrj 2019/7/26
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    val mException: MutableLiveData<Exception> = MutableLiveData()

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI { tryCatch(tryBlock, {}, {}, true) }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    mException.value = e
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

    //统一的响应错误处理
    suspend fun executeResponse(response: HttpResult<Any>, method: String, successBlock: suspend CoroutineScope.() -> Unit, errorBlock: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            if (response.status == 0) {
                successBlock()
            } else {
                errorBlock()
            }
        }
    }
}