package com.kotlin.mvvm.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.base.Loge
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.databinding.ActivitySplashBinding
import com.kotlin.mvvm.error.ExceptionHandler
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.callRequestTest
import com.kotlin.mvvm.network.handlerResponse
import com.kotlin.mvvm.network.handlerResponseTest
import com.kotlin.mvvm.network.handlerResponseTest2
import com.kotlin.mvvm.network.safeApiCallWithResult
import com.kotlin.mvvm.ui.home.HomeViewModel
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private lateinit var splashScreen: SplashScreen
    private val defaultExitDuration = 1000L

    override fun setWindowConfigure() {
        splashScreen = installSplashScreen()
    }

    override fun getContentView() = binding.root
    val handler = Handler(Looper.getMainLooper()) {
        Loge.e(it.what.toString())
        true
    }

    override fun initView(bundle: Bundle?) {
        initImmersionBar(ContextCompat.getColor(this, R.color.colorAccent))
//        splashScreen.setOnExitAnimationListener { provider ->
//            showSplashIconExitAnimator(provider.iconView) {
//                provider.remove()
//            }
        // 两个动画的时长可以不一样，这里监听 splashScreen 动画结束
//            showSplashExitAnimator(provider.view) {
//                provider.remove()
        // 进入主界面，顺便给个 FadeOut 退场动画
        handler.postDelayed({
            ActivityUtils.startActivity(MainActivity::class.java)
            ActivityUtils.finishActivity(this)
        }, 5)
//                overridePendingTransition(0, R.anim.fade_out)
        //  }
//        }
    }

    // Show exit animator for splash icon.
    @SuppressLint("Recycle")
    private fun showSplashIconExitAnimator(iconView: View, onExit: () -> Unit = {}) {
        val alphaOut = ObjectAnimator.ofFloat(
            iconView,
            View.ALPHA,
            1f,
            0f
        )
        // Bird scale out animator.
        val scaleOut = ObjectAnimator.ofFloat(
            iconView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0.3f, 0.3f)
            }
        )
        AnimatorSet().run {
            interpolator = AnticipateInterpolator()
            duration = defaultExitDuration
            playTogether(alphaOut, scaleOut)
            doOnEnd {
                onExit()
            }
            start()
        }
    }

    // Show exit animator for splash screen view.
    @SuppressLint("Recycle")
    private fun showSplashExitAnimator(splashScreenView: View, onExit: () -> Unit = {}) {
        // Create your custom animation set.
        val alphaOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.ALPHA,
            1f,
            0f
        )
        val scaleOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0.3f, 0.3f)
            }
        )
        AnimatorSet().run {
            interpolator = AnticipateInterpolator()
            duration = defaultExitDuration
            playTogether(scaleOut, alphaOut)
            doOnEnd {
                onExit()
            }
            start()
        }
    }

    private val homeViewModel by viewModels<HomeViewModel>()
    override fun initData() {

        binding.splashTv.setOnClickListener {

            lifecycleScope.launch {


//                callRequestTest {
//                    handlerResponseTest(RetrofitFactory.instance.service.getBannerJson())
//                }.fold({
//                    it.forEach {
//                        Loge.e(it.toString())
//                    }
//                }, {
//                    val ex = ExceptionHandler.handleException(it)
//                    Loge.e(ex.errMsg + "-sp:ex-" + ex.errMsg)
//                })


                try {
                    val result =
                        callRequestTest {
                            handlerResponseTest2({ RetrofitFactory.instance.service.getBannerJson() })
                        }
                    result.fold(
                        {
                            Loge.e(it.toString())
                        },
                        {
                            ExceptionHandler.handleException(it).let {
                                Loge.e(it.errMsg + ":" + it.errCode)
                            }
                        }
                    )
                } catch (e: Throwable) {
                    val ex = ExceptionHandler.handleException(e)
                    Loge.e(ex.errMsg + ":" + ex.errCode)
                }
            }

        }
    }
}