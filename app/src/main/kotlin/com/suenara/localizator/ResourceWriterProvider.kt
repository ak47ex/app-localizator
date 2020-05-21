package com.suenara.localizator

import com.suenara.localizator.stringres.StringResWriter
import java.util.*

interface ResourceWriterProvider {
    fun getStringResWriter(locale: Locale): StringResWriter
}