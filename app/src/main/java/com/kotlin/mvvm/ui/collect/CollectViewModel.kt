package com.kotlin.mvvm.ui.collect

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.base.Loge
import com.kotlin.mvvm.common.base.BaseListResponse
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.handlerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 13:53
 */
class CollectViewModel : BaseViewModel() {

    val mCollectBean = MutableLiveData<BaseListResponse<MutableList<CollectBean>>>()

    fun getCollectList(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getCollectList(page)) }
        }
//        val baseResponse = withTimeout(10000) {
//            callRequest { handlerResponse(RetrofitFactory.instance.service.getCollectList(page)) }
//        }

        baseResponse?.fold({
            mCollectBean.value = it
        }, {
            ToastUtils.showShort(it.message)
            Loge.e(it.message)
        })
    }

    fun collect(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.collectList(id)) }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun unCollect(id: Int, originId: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.unCollect(id, originId)) }
        }
        Loge.e("baseResponse:"+baseResponse.hashCode())
        baseResponse.fold({
            handlerCode.value = handler_code_un_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}