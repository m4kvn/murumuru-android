package com.m4kvn.murumuru.ui.main

import jp.satorufujiwara.binder.ViewType

enum class HomeViewType : ViewType {
    SECTION_TITLE,
    ITEM;

    override fun viewType(): Int = ordinal
}