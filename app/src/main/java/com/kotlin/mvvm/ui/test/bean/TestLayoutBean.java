package com.kotlin.mvvm.ui.test.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TestLayoutBean<T> implements MultiItemEntity {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //输入框
    public static final int EDIT = 0;
    //复选框
    public static final int CHECK = 1;
    //选择框
    public static final int SELECT = 2;
    //item类型
    private int fieldType;

    public TestLayoutBean(int fieldType) {
        //将传入的type赋值
        this.fieldType = fieldType;
    }

    @Override
    public int getItemType() {
        //返回传入的itemType
        return fieldType;
    }
}
