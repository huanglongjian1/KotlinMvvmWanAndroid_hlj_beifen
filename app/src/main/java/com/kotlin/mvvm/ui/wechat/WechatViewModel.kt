package com.kotlin.mvvm.ui.wechat

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.base.BaseListResponse
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.error.ApiException
import com.kotlin.mvvm.error.ExceptionHandler
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.callRequestTest
import com.kotlin.mvvm.network.handlerResponse
import com.kotlin.mvvm.network.handlerResponseTest
import com.kotlin.mvvm.ui.wechat.bean.WechatBean
import com.kotlin.mvvm.ui.wechat.bean.WechatPagerBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/22 15:57
 */
class WechatViewModel : BaseViewModel() {

    val mWechatBean = MutableLiveData<MutableList<WechatBean>>()
    val mWechatPagerBean = MutableLiveData<BaseListResponse<MutableList<WechatPagerBean>>>()

    val mApiException = MutableLiveData<ApiException>()
    fun getWechatArticleJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest {
                handlerResponse(RetrofitFactory.instance.service.getWechatArticleJson())
            }
        }
        baseResponse.fold({
            mWechatBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getUserWechatArticleJson(user_id: Int?, page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequestTest {
                handlerResponseTest(
                    RetrofitFactory.instance.service.getUserWechatArticleJson(
                        user_id,
                        page
                    )
                )
            }
        }
        baseResponse.fold({
            mWechatPagerBean.value = it
        }, {
            ToastUtils.showShort(it.message)
            mApiException.value=ExceptionHandler.handleException(it)
        })
    }

    fun collect(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest {
                handlerResponse(RetrofitFactory.instance.service.collectList(id))
            }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun unCollectList(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest {
                handlerResponse(RetrofitFactory.instance.service.unCollectList(id))
            }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_un_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}