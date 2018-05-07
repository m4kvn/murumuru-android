package com.m4kvn.murumuru.ui.main

import jp.satorufujiwara.binder.Section

enum class MainSection : Section {
    PROFILE,
    LOGIN_BUTTON,
    TITLE_NEW,
    SAMPLES;

    override fun position() = ordinal
}