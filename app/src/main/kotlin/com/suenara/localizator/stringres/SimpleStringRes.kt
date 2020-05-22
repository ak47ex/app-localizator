package com.suenara.localizator.stringres

class SimpleStringRes() : MutableStringRes {

    private val values = LinkedHashMap<String, String>()

    constructor(prototype: StringRes) : this() {
        prototype.keySet().forEach {
            values[it] = requireNotNull(prototype[it])
        }
    }

    override fun set(key: String, value: String?) {
        value?.let {
            values[key] = value
        } ?: values.remove(key)
    }

    override fun clear() {
        values.clear()
    }

    override fun keySet(): List<String> = values.keys.toList()

    override fun get(key: String): String? = values[key]
}