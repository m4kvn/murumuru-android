package com.m4kvn.murumuru.ui.main

import jp.satorufujiwara.binder.Section

enum class HomeSection : Section {
    NEW;

    override fun position(): Int = ordinal
}