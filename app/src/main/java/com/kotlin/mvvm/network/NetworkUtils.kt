package com.kotlin.mvvm.network

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.Loge
import com.kotlin.mvvm.common.base.BaseResponse
import com.kotlin.mvvm.common.base.BaseResult
import com.kotlin.mvvm.error.ApiException
import com.kotlin.mvvm.error.ExceptionHandler
import com.kotlin.mvvm.ext.d
import com.kotlin.mvvm.ui.home.bean.BannerBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import okio.IOException

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/3/17 11:20
 */

/**
 * 请求
 */
suspend fun <T : Any> callRequest(
    call: suspend () -> BaseResult<T>
): BaseResult<T> {
    return try {

        call()


    } catch (e: Exception) {
        //这里统一处理异常
        e.printStackTrace()
        val recordMethodName = Thread.currentThread().stackTrace
        d("className = ${recordMethodName[4].className} fileName = ${recordMethodName[4].fileName} lineNumber = ${recordMethodName[4].lineNumber} methodName = ${recordMethodName[4].methodName}")
        Loge.e("网络错误:" + e.message)
        BaseResult.failure(if (!NetworkUtils.isConnected()) IOException(StringUtils.getString(R.string.common_connect_error)) else e)
    }
}

/**
 * 处理返回结果
 *
 *  请注意：当前在IO线程
 *  @param successBlock  成功之后处理
 *  @param errorBlock    失败之后处理
 */
suspend fun <T : Any> handlerResponse(
    response: BaseResponse<T>,
    successBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend () -> Unit)? = null
): BaseResult<T> {
//    return coroutineScope {
    return if (response.errorCode == 0) {
        val recordMethodName = Thread.currentThread().stackTrace
        d("className = ${recordMethodName[4].className} fileName = ${recordMethodName[4].fileName} lineNumber = ${recordMethodName[4].lineNumber} methodName = ${recordMethodName[4].methodName}")
        // 成功之后处理
        successBlock?.let { it() }
        BaseResult.success(response.data)
    } else {
        val recordMethodName = Thread.currentThread().stackTrace
        d("className = ${recordMethodName[4].className} fileName = ${recordMethodName[4].fileName} lineNumber = ${recordMethodName[4].lineNumber} methodName = ${recordMethodName[4].methodName}")
        // 失败之后处理
        errorBlock?.let { it() }
        BaseResult.failure(Exception("Failed to ${recordMethodName[4].className}${response.errorMsg}"))
    }
//    }
}

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/3/17 11:20
 */

/**
 * 请求
 */
suspend fun <T : Any> callRequestTest(
    call: suspend () -> T?
): BaseResult<T> {
    return try {


        BaseResult.success(call()!!)


    } catch (e: Exception) {
        Loge.e("网络错误:" + e.message)
        val ex = ExceptionHandler.handleException(e)
        BaseResult.failure(ex)
    }
}

/**
 * 处理返回结果
 *
 *  请注意：当前在IO线程
 *  @param successBlock  成功之后处理
 *  @param errorBlock    失败之后处理
 */
suspend fun <T : Any> handlerResponseTest(
    response: BaseResponse<T>,
    successBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend () -> Unit)? = null
): T? {
    try {
        val result = withContext(Dispatchers.IO) {
            withTimeout(10) {
                response
            }
        } ?: return null

        if (result.errorCode != 0) {
            errorBlock?.invoke()
            throw ApiException(result.errorCode, result.errorMsg)
        }
        successBlock?.invoke()
        return result.data
    } catch (e: Throwable) {
        val exception = ExceptionHandler.handleException(e)
        Loge.e(exception.errMsg + ":" + exception.errCode)
    }
    return null
}

suspend fun <T : Any> handlerResponseTest2(
    response: suspend () -> BaseResponse<T>?,
    successBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend () -> Unit)? = null
): T? {
    try {
        val result = withContext(Dispatchers.IO) {
            withTimeout(10000) {
                response()
            }
        } ?: return null

        if (result.errorCode != 0) {
            errorBlock?.invoke()
            throw ApiException(result.errorCode, result.errorMsg)
        }
        successBlock?.invoke()
        return result.data
    } catch (e: Throwable) {
        val exception = ExceptionHandler.handleException(e)
        Loge.e(exception.errMsg + ":" + exception.errCode)
    }
    return null
}

/**
 * 不依赖BaseRepository，需要在作用域中运行
 * @param errorCall 错误回调
 * @param responseBlock 请求函数
 */
suspend fun <T> safeApiCallWithResult(
    errorBlock: (suspend () -> Unit)? = null,
    responseBlock: suspend () -> BaseResponse<T>?
): T? {
    try {
        val response = withContext(Dispatchers.IO) {
            withTimeout(10) {
                responseBlock()
            }
        } ?: return null

        if (response.errorCode != 0) {
            throw ApiException(response.errorCode, response.errorMsg)
        }
        return response.data
    } catch (e: Exception) {
        e.printStackTrace()
        val exception = ExceptionHandler.handleException(e)
        Loge.e(exception.errMsg + ":" + exception.errCode)
    }
    return null
}