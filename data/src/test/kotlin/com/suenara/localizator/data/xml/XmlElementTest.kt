package com.suenara.localizator.data.xml

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse

private const val VALID_ELEMENT_TAG_1 = "tag"
private const val VALID_ELEMENT_TAG_2 = "_tag"
private const val VALID_ELEMENT_TAG_3 = "_749"
private const val VALID_ELEMENT_TAG_4 = "_749aBcDEF"

private const val VALID_ELEMENT_ATTRIBUTE_1_NAME = "attribute1"
private const val VALID_ELEMENT_ATTRIBUTE_1_VALUE = "first attribute value"
private const val VALID_ELEMENT_ATTRIBUTE_2_NAME = "attribute2"
private const val VALID_ELEMENT_ATTRIBUTE_2_VALUE = ""
private const val VALID_ELEMENT_CONTENT = "some element content"

private const val INVALID_ELEMENT_TAG_1 = "1tag"
private const val INVALID_ELEMENT_TAG_2 = ".tag"
private const val INVALID_ELEMENT_TAG_3 = "∆åœ∑ß"

private const val VALID_XML =
    "<$VALID_ELEMENT_TAG_1 $VALID_ELEMENT_ATTRIBUTE_1_NAME=\"$VALID_ELEMENT_ATTRIBUTE_1_VALUE\" $VALID_ELEMENT_ATTRIBUTE_2_NAME=\"$VALID_ELEMENT_ATTRIBUTE_2_VALUE\">$VALID_ELEMENT_CONTENT</$VALID_ELEMENT_TAG_1>"

class XmlElementTest {

    @Test
    fun `test valid element tag`() {
        assert(XmlElement("<$VALID_ELEMENT_TAG_1/>").tag == VALID_ELEMENT_TAG_1)
        assert(XmlElement("<$VALID_ELEMENT_TAG_2/>").tag == VALID_ELEMENT_TAG_2)
        assert(XmlElement("<$VALID_ELEMENT_TAG_3/>").tag == VALID_ELEMENT_TAG_3)
        assert(XmlElement("<$VALID_ELEMENT_TAG_4/>").tag == VALID_ELEMENT_TAG_4)

        assert(XmlElement("<$VALID_ELEMENT_TAG_1></$VALID_ELEMENT_TAG_1>").tag == VALID_ELEMENT_TAG_1)
        assert(XmlElement("<$VALID_ELEMENT_TAG_2></$VALID_ELEMENT_TAG_2>").tag == VALID_ELEMENT_TAG_2)
        assert(XmlElement("<$VALID_ELEMENT_TAG_3></$VALID_ELEMENT_TAG_3>").tag == VALID_ELEMENT_TAG_3)
        assert(XmlElement("<$VALID_ELEMENT_TAG_4></$VALID_ELEMENT_TAG_4>").tag == VALID_ELEMENT_TAG_4)
    }

    @Test
    fun `test invalid element tag`() {
        assertFails { XmlElement("<$INVALID_ELEMENT_TAG_1/>") }
        assertFails { XmlElement("<$INVALID_ELEMENT_TAG_2/>") }
        assertFails { XmlElement("<$INVALID_ELEMENT_TAG_3/>") }

        assertFails { XmlElement("<$INVALID_ELEMENT_TAG_1></$INVALID_ELEMENT_TAG_1>") }
        assertFails { XmlElement("<$INVALID_ELEMENT_TAG_2></$INVALID_ELEMENT_TAG_2>") }
        assertFails { XmlElement("<$INVALID_ELEMENT_TAG_3></$INVALID_ELEMENT_TAG_3>") }
    }

    @Test
    fun `test valid attributes`() {
        XmlElement(VALID_XML).apply {
            assert(attributes[VALID_ELEMENT_ATTRIBUTE_1_NAME] == VALID_ELEMENT_ATTRIBUTE_1_VALUE) { "Wrong attribute value ${attributes[VALID_ELEMENT_ATTRIBUTE_1_NAME]}" }
            assert(attributes[VALID_ELEMENT_ATTRIBUTE_2_NAME] == VALID_ELEMENT_ATTRIBUTE_2_VALUE) { "Wrong attribute value ${attributes[VALID_ELEMENT_ATTRIBUTE_2_NAME]}" }
        }
    }

    @Test
    fun `test empty attributes`() {
        assert(XmlElement("<$VALID_ELEMENT_TAG_1></$VALID_ELEMENT_TAG_1>").attributes.isEmpty())
        assert(XmlElement("<$VALID_ELEMENT_TAG_1/>").attributes.isEmpty())
    }

    @Test
    fun `test valid value`() {
        assert(XmlElement(VALID_XML).getStringValue() == VALID_ELEMENT_CONTENT)

        val deepElement = "<$VALID_ELEMENT_TAG_1>$VALID_ELEMENT_CONTENT</$VALID_ELEMENT_TAG_1>"
        assert(XmlElement(deepElement).getStringValue() == VALID_ELEMENT_CONTENT)

        val midElement = "<$VALID_ELEMENT_TAG_2>$deepElement</$VALID_ELEMENT_TAG_2>"
        assert(XmlElement(midElement).getStringValue() == deepElement)

        val topElement = "<$VALID_ELEMENT_TAG_3>$midElement</$VALID_ELEMENT_TAG_3>"
        assert(XmlElement(topElement).getStringValue() == midElement)
    }

    @Test
    fun `test empty value`() {
        assert(XmlElement("<$VALID_ELEMENT_TAG_1></$VALID_ELEMENT_TAG_1>").getStringValue()?.isEmpty() == true)
    }

    @Test
    fun `test null value`() {
        assert(XmlElement("<$VALID_ELEMENT_TAG_1/>").getStringValue() == null)
    }

    @Test
    fun `test string representation`() {
        assert(XmlElement(VALID_XML).asString() == VALID_XML)
    }

    @Test
    fun `test equals`() {
        assert(XmlElement(VALID_XML) == XmlElement(VALID_XML))
        assert(XmlElement("<$VALID_ELEMENT_TAG_1/>") == XmlElement("<$VALID_ELEMENT_TAG_1/>"))
        assert(XmlElement("<$VALID_ELEMENT_TAG_1></$VALID_ELEMENT_TAG_1>") == XmlElement("<$VALID_ELEMENT_TAG_1></$VALID_ELEMENT_TAG_1>"))

        assertFalse { XmlElement(VALID_XML) == XmlElement("<$VALID_ELEMENT_TAG_1/>") }
        assertFalse { XmlElement("<$VALID_ELEMENT_TAG_1></$VALID_ELEMENT_TAG_1>") == XmlElement("<$VALID_ELEMENT_TAG_1/>") }
    }
}