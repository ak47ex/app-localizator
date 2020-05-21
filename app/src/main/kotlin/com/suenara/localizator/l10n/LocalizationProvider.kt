package com.suenara.localizator.l10n

import com.suenara.localizator.stringres.StringRes
import java.util.*

interface LocalizationProvider {
    fun getAvailableLocalizations(): List<Locale>
    fun getLocalization(locale: Locale): StringRes?
}