package com.m4kvn.murumuru.ui.main

import jp.satorufujiwara.binder.ViewType

enum class MainViewType : ViewType {
    SECTION_TITLE,
    LOGIN_BUTTON,
    PROFILE,
    SECTION_MEDIA_ITEM;

    override fun viewType() = ordinal
}