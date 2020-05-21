package com.suenara.localizator.stringres

interface StringRes {
    fun keySet(): List<String>
    operator fun get(key: String): String?
}