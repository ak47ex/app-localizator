package com.suenara.localizator.stringres

interface MutableStringRes : StringRes {
    operator fun set(key: String, value: String?)
    fun clear()
}