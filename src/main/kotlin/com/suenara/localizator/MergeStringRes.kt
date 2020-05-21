package com.suenara.localizator

import com.suenara.localizator.stringres.MutableStringRes
import com.suenara.localizator.stringres.StringRes
import com.suenara.localizator.stringres.StringResWriter

class MergeStringRes(
    private val source: StringRes,
    private val destination: MutableStringRes,
    private val writer: StringResWriter
) {
    fun merge() {
        val nonLocalized = mutableListOf<Pair<String, String?>>()
        destination.keySet().forEach {
            if (source[it] == null) nonLocalized.add(it to destination[it])
        }
        destination.clear()
        source.keySet().forEach {
            destination[it] = source[it]
        }
        nonLocalized.forEach { (key, value) ->
            destination[key] = value
        }
        writer.write(destination)
    }
}