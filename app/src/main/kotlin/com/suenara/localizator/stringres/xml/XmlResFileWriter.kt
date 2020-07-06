package com.suenara.localizator.stringres.xml

import com.suenara.localizator.data.xml.XmlElement
import com.suenara.localizator.stringres.StringRes
import com.suenara.localizator.stringres.StringResWriter
import java.io.File
import java.io.FileWriter

class XmlResFileWriter(private val output: File) : StringResWriter {
    override fun write(stringRes: StringRes) {
        FileWriter(output, false).use { printer ->
            printer.appendln(FILE_HEADER)
            printer.appendln(
                OPEN_TAG.format(
                    RESOURCES_TAG
                )
            )
            stringRes.keySet().map {
                it to stringRes[it]
            }.forEach { (key, value) ->
                val line = if (key.startsWith(COMMENT_TAG)) {
                    XmlElement.Builder(key).comment().build()
                } else {
                    XmlElement.Builder(STRING_VALUE_TAG)
                        .addAttribute(NAME_ATTRIBUTE, key)
                        .setValue(value)
                        .build()
                }
                printer.appendln("$VALUES_INTENT$line")
            }
            printer.append(
                CLOSE_TAG.format(
                    RESOURCES_TAG
                )
            )
        }
    }

    companion object {
        private const val FILE_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
        private const val STRING_VALUE_TAG = "string"
        private const val RESOURCES_TAG = "resources"
        private const val OPEN_TAG = "<%s>"
        private const val CLOSE_TAG = "</%s>"
        private const val VALUES_INTENT = "    "
        private const val NAME_ATTRIBUTE = "name"

        //TODO: rework comments
        private const val COMMENT_TAG = "<!--"
    }
}