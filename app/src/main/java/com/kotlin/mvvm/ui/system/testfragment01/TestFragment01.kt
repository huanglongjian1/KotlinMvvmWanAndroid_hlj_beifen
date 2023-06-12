package com.kotlin.mvvm.ui.system.testfragment01

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.base.Loge
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.databinding.FragmentSystemOneBinding
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequestTest
import com.kotlin.mvvm.network.handlerResponseTest
import kotlinx.coroutines.launch

class TestFragment01 : BaseFragment() {
    private val binding by lazy {
        FragmentSystemOneBinding.inflate(layoutInflater)
    }

    override fun getContentView(): View? {
        return binding.root
    }

    override fun initView(bundle: Bundle?) {
        Loge.e("onViewCreated")
    }

    override fun onResume() {
        super.onResume()

        binding.testTv.setOnClickListener {

            lifecycleScope.launch {


                callRequestTest {


                    handlerResponseTest(

                            response = RetrofitFactory.instance.service.getBannerJson()

                    )


                }.fold({
                    Loge.e(it.toString())
                }, {
                    Loge.e(it.message)
                })


            }


        }


    }

    override fun initData() {

    }
}