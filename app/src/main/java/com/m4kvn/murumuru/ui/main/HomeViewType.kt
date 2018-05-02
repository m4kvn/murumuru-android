package com.m4kvn.murumuru.ui.main

import jp.satorufujiwara.binder.ViewType

enum class HomeViewType : ViewType {
    ITEM;

    override fun viewType(): Int = ordinal
}