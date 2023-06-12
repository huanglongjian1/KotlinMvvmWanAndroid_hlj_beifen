package com.kotlin.mvvm.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.base.Loge
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.base.BaseListResponse
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.error.ExceptionHandler
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.callRequestTest
import com.kotlin.mvvm.network.handlerResponse
import com.kotlin.mvvm.network.handlerResponseTest
import com.kotlin.mvvm.ui.home.bean.BannerBean
import com.kotlin.mvvm.ui.home.bean.HomeBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/7 11:46
 */
class HomeViewModel : BaseViewModel() {

    private val bannerUrl = arrayListOf<String>()
    val mBannerLists = MutableLiveData<MutableList<String>>()
    val mHomeBeans = arrayListOf<HomeBean>()
    val mDataBeans = MutableLiveData<BaseListResponse<MutableList<HomeBean>>>()

    fun getBannerJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {

            callRequest {
                handlerResponse(
                    RetrofitFactory.instance.service.getBannerJson(),
                    { Loge.e("访问成功") },
                    { Loge.e("访问失败") })
            }


        }

//        val baseResponse = withTimeout(5000) {
//            callRequest { handlerResponse(RetrofitFactory.instance.service.getBannerJson()) }
//        }
        Loge.e("访问--------结果")
        baseResponse.fold({
            Loge.e("返回结果:" + it.toString())
            setBannerUrl(it)
        }, {
            Loge.e("错误:" + it.message)
            ToastUtils.showShort(it.message)
        })
    }

    private fun setBannerUrl(data: List<BannerBean>) {
        if (data.isNotEmpty()) {
//            Observable.fromIterable(data).subscribe(object : Observer<BannerBean> {
//
//                override fun onSubscribe(d: Disposable) {
//                    bannerUrl.clear()
//                    if (null != mBannerLists.value) {
//                        mBannerLists.value?.clear()
//                    }
//                }
//
//                override fun onNext(t: BannerBean) {
//                    bannerUrl.add(t.imagePath)
//                }
//
//                override fun onError(e: Throwable) {
//                    uiState.value = UiState.LoadError
//                }
//
//                override fun onComplete() {
//                    mBannerLists.value = bannerUrl
//                }
//            })
            data.asFlow().map {
                bannerUrl.add(it.imagePath)
            }.catch {
                val mApiException = ExceptionHandler.handleException(it)
                Loge.e(mApiException.errMsg + "-:-" + mApiException.errCode)
                uiState.value = UiState.LoadError
            }.onStart {
                bannerUrl.clear()
                if (null != mBannerLists.value) {
                    mBannerLists.value?.clear()
                }
            }
                .onCompletion {
                    mBannerLists.value = bannerUrl
                }.launchIn(viewModelScope)

        }
    }

    fun getTopBeanJson(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getTopJson()) }
        }
        baseResponse.fold({
            reHomeBeanData(it, page)
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    private fun reHomeBeanData(data: List<HomeBean>, page: Int) {
        if (data.isNotEmpty()) {
//            Observable.fromIterable(data).subscribe(object : Observer<HomeBean> {
//                override fun onSubscribe(d: Disposable) {
//                    mHomeBeans.clear()
//                }
//
//                override fun onNext(t: HomeBean) {
//                    t.top = true
//                }
//
//                override fun onError(e: Throwable) {
//                }
//
//                override fun onComplete() {
//                    if (page == 0) {
//                        mHomeBeans.clear()
//                        mHomeBeans.addAll(data)
//                    }
//                    getArticleJson(page)
//                }
//            })
            data.asFlow().map {
                it.top = true
            }.onStart {
                mHomeBeans.clear()
            }
                .onCompletion {
                    if (page == 0) {
                        mHomeBeans.clear()
                        mHomeBeans.addAll(data)
                    }
                    getArticleJson(page)
                }.launchIn(viewModelScope)
        }
    }

    private fun getArticleJson(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getArticleJson(page)) }
        }
        baseResponse.fold({
            mHomeBeans.addAll(it.datas)
            it.datas.clear()
            it.datas.addAll(mHomeBeans)
            mDataBeans.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun collect(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequestTest { handlerResponseTest(RetrofitFactory.instance.service.collectList(id)) }
        }// 测试
        baseResponse.fold({
            handlerCode.value = handler_code_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun unCollectList(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequestTest { handlerResponseTest(RetrofitFactory.instance.service.unCollectList(id)) }
        }//测试
        baseResponse.fold({
            handlerCode.value = handler_code_un_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}