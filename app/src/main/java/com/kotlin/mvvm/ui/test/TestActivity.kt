package com.kotlin.mvvm.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.base.Loge
import com.kotlin.mvvm.databinding.ActivityTestBinding
import com.kotlin.mvvm.popup.LoadPopup
import com.kotlin.mvvm.popup.LoadingView
import com.kotlin.mvvm.ui.test.bean.TestLayoutBean

class TestActivity : BaseActivity() {
    private val mBinding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }
    private val testAdapter by lazy {
        TestAdapter()
    }

    override fun getContentView(): View? {
        return mBinding.root
    }

    override fun initView(bundle: Bundle?) {
        Loge.e("initView")
        mBinding.testRecyclerView.apply {
            adapter = testAdapter
            layoutManager = LinearLayoutManager(this@TestActivity)
        }
        mBinding.testActivityTv.setOnClickListener {
          LoadPopup(this)

        }

    }

    val data = mutableListOf<TestLayoutBean<Any>>()
    override fun initData() {
        for (i in 0..5) {
            val testLayoutBean = TestLayoutBean<Any>(0)
            testLayoutBean.data = "$i-hlj"
            data.add(testLayoutBean)
        }
        for (i in 0..3) {
            val testLayoutBean = TestLayoutBean<Any>(1)
            testLayoutBean.data = i
            data.add(testLayoutBean)
        }

        testAdapter.loadMoreModule.setOnLoadMoreListener {
            Loge.e("loadmore")
        }
        testAdapter.data = data.toMutableList()
        val emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty_recycler, null)
        testAdapter.setEmptyView(
            emptyView
        )
        val emptyTV = emptyView.findViewById<TextView>(R.id.tv_empty)
        emptyTV.setOnClickListener {

            testAdapter.data = data.toMutableList()
            testAdapter.notifyDataSetChanged()
        }
    }
}