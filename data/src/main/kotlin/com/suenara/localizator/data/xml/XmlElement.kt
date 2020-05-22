package com.suenara.localizator.data.xml

class XmlElement(string: String) {
    val tag: String
    val attributes: Map<String, String>

    private val rawValue: String?

    init {
        val origin: String = string.trim()

        require(origin.startsWith(OPEN_TAG)) { "String should start with \'$OPEN_TAG\'" }
        require(origin.endsWith(END_TAG)) { "String should end with \'$END_TAG\'" }

        tag = extractName(origin)
        attributes = extractAttributes(origin).toMap(LinkedHashMap())
        rawValue = extractValueString(origin)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? XmlElement)?.let {
            tag == it.tag && attributes == it.attributes && rawValue == it.rawValue
        } ?: false
    }

    override fun toString(): String {
        return "XmlElement(tag=$tag, attributes=$attributes, rawValue=$rawValue)"
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + attributes.hashCode()
        result = 31 * result + (rawValue?.hashCode() ?: 0)
        return result
    }

    fun getStringValue(): String? = rawValue

    fun asString(): String = Builder(tag).apply {
        attributes.forEach { (key, value) ->
            addAttribute(key, value)
        }
        setValue(rawValue)
    }.build()

    class Builder(private val tag: String) {
        private val attributes = mutableListOf<Pair<String, String>>()

        private var value: String? = null

        fun addAttribute(key: String, value: String) = apply { attributes.add(key to value) }

        fun setValue(value: String?) = apply { this.value = value }

        fun build(): String = StringBuilder().apply {
            append("$OPEN_TAG$tag")
            attributes.forEach { (key, value) ->
                append(" $key=\"$value\"")
            }
            if (value != null) {
                append(END_TAG)
                append(value)
                append("$OPEN_TAG/$tag$END_TAG")
            } else {
                append("/$END_TAG")
            }
        }.toString()
    }

    companion object {
        private const val OPEN_TAG = '<'
        private const val END_TAG = '>'

        private fun extractName(string: CharSequence): String {
            require(OPEN_TAG == string[0])

            var nameEnd = -1
            for (index in 1 until string.length) {
                if (!(string[index].isLetterOrDigit() || string[index] == '_')) {
                    nameEnd = index
                    break
                }
            }
            if (nameEnd == -1) throw IllegalStateException("No name in element $string")
            return string.substring(1, nameEnd).apply {
                if (isEmpty() || !first().run { isLetter() || this == '_' }) {
                    throw IllegalStateException("Malformed xml! Element should start with letter or underscore: `$this`. Source string is `$string`.")
                }
            }
        }

        private fun extractAttributes(string: CharSequence): List<Pair<String, String>> {
            val skipLength = 1 + extractName(string).length
            var endIndex = skipLength
            for (index in skipLength until string.length) {
                if (string[index] == END_TAG && string[index - 1] != '\\') {
                    endIndex = index
                    break
                }
            }
            if ((skipLength until endIndex).run { first == last }) {
                return emptyList()
            }

            val wordBuilder = StringBuilder()
            var isName = true
            var isValue = false
            val nameList = mutableListOf<String>()
            val attributeList = mutableListOf<String>()
            loop@ for (index in skipLength until endIndex) {
                when {
                    isName -> {
                        if (string[index].isWhitespace()) {
                            continue@loop
                        } else if (string[index].isLetterOrDigit()) {
                            wordBuilder.append(string[index])
                        } else if (string[index] == '=') {
                            nameList.add(wordBuilder.toString())
                            wordBuilder.clear()
                            isName = false
                            isValue = true
                        } else {
                            throw IllegalStateException("Malformed xml at $index ${string[index]} line $string")
                        }
                    }
                    isValue -> {
                        if (string[index] == '\"' || string[index] == '\'') {
                            if (string[index - 1] == '\\') {
                                wordBuilder.append(string[index])
                            } else {
                                if (wordBuilder.isEmpty() && string[index - 1] != string[index]) {
                                    continue@loop
                                } else {
                                    attributeList.add(wordBuilder.toString())
                                    wordBuilder.clear()
                                    isName = true
                                    isValue = false
                                }
                            }
                        } else {
                            wordBuilder.append(string[index])
                        }
                    }
                }
            }

            return nameList.zip(attributeList)
        }

        private fun extractValueString(string: CharSequence): String? {
            val name = extractName(string).reversed()

            var startOfValue = name.length
            for (index in (1 + name.length) until string.length) {
                if (string[index] == END_TAG) {
                    if (string[index - 1] == '/') return null
                    else if (string[index - 1] == '\\') continue
                    else {
                        startOfValue = index + 1
                        break
                    }
                }
            }

            var endOfValue = string.length
            for (index in (string.length - 1) downTo name.length) {
                if (string[index] == name[0]) {
                    var equals = true
                    for (subIndex in name.indices) {
                        if (string[index - subIndex] != name[subIndex]) {
                            equals = false
                            break
                        }
                    }
                    if (equals && string[index - name.length] == '/' && string[index - name.length - 1] == OPEN_TAG) {
                        endOfValue = index - name.length - 1
                    }
                }
            }

            return string.substring(startOfValue, endOfValue)
        }
    }
}