package com.kotlin.mvvm.ui.test

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ui.test.bean.TestLayoutBean

class TestAdapter : BaseMultiItemQuickAdapter<TestLayoutBean<Any>, BaseViewHolder>(),LoadMoreModule {



    init {
        addItemType(TestLayoutBean.EDIT, R.layout.test_one_item);
        addItemType(TestLayoutBean.CHECK, R.layout.test_two_item);
    }

    override fun convert(holder: BaseViewHolder, item: TestLayoutBean<Any>) {
        when (item.itemType) {
            TestLayoutBean.EDIT -> {
                holder.setText(R.id.test_one_tv, item.data.toString())
            }

            TestLayoutBean.CHECK -> {
                holder.setText(R.id.test_two_tv1, item.data.toString())
                    .setText(R.id.test_two_tv2, "item2_tv2:"+item.data)
            }
        }
    }
}


class BaseMultiQuickItem(override val itemType: Int, val data: Any) : MultiItemEntity {
    companion object {
        const val FIRST_TYPE = 1
        const val SECOND_TYPE = 2
    }
}
