package com.suenara.localizator.stringres.xml

import com.suenara.localizator.data.xml.XmlElement
import com.suenara.localizator.stringres.MutableStringRes
import com.suenara.localizator.stringres.StringRes
import java.io.File

class XmlFileStringRes(file: File) : MutableStringRes {

    private val resourceMap: MutableMap<String, XmlElement> = extractResourceMap(
        file
    ).toMutableMap()

    override fun keySet(): List<String> = resourceMap.keys.toList()
    override fun get(key: String): String? = resourceMap[key]?.getStringValue()

    override fun equals(other: Any?): Boolean {
        return (other as? StringRes)?.let { otherRes ->
            keySet().map { it to get(it) } == otherRes.keySet().map { it to otherRes[it] }
        } ?: false
    }

    override fun hashCode(): Int {
        return resourceMap.hashCode()
    }

    override fun set(key: String, value: String?) {
        value?.let {
            resourceMap[key] = XmlElement.Builder(STRING_VALUE_TAG)
                .addAttribute(NAME_ATTRIBUTE, key)
                .setValue(value)
                .build().let {
                    XmlElement(it)
                }
        } ?: resourceMap.remove(key)
    }

    override fun clear() {
        resourceMap.clear()
    }

    companion object {
        private const val STRING_VALUE_TAG = "string"
        private const val RESOURCES_TAG = "resources"
        private const val OPEN_TAG = "<%s>"
        private const val CLOSE_TAG = "</%s>"
        private const val NAME_ATTRIBUTE = "name"

        private fun extractResourceMap(file: File): Map<String, XmlElement> {
            val map = LinkedHashMap<String, XmlElement>()

            file.useLines { lines ->
                var extractKeys = false
                val startTag = OPEN_TAG.format(
                    RESOURCES_TAG
                )
                val closeTag = CLOSE_TAG.format(
                    RESOURCES_TAG
                )
                for (line in lines) {
                    if (!extractKeys) {
                        if (line.startsWith(startTag)) {
                            extractKeys = true
                        }
                        continue
                    }
                    if (extractKeys && line.startsWith(closeTag)) {
                        break
                    }
                    XmlElement(line).let {
                        map[requireNotNull(it.attributes[NAME_ATTRIBUTE])] = it
                    }
                }
            }
            return map
        }
    }
}