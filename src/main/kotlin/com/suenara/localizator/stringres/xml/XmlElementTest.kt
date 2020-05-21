package com.suenara.localizator.stringres.xml


private const val TEST_ELEMENT_TAG = "tag"
private const val TEST_ELEMENT_ATTRIBUTE_1_NAME = "attribute1"
private const val TEST_ELEMENT_ATTRIBUTE_1_VALUE = "first attribute value"
private const val TEST_ELEMENT_ATTRIBUTE_2_NAME = "attribute2"
private const val TEST_ELEMENT_ATTRIBUTE_2_VALUE = ""

fun `test valid xml parse`() {
    val testString = "<$TEST_ELEMENT_TAG $TEST_ELEMENT_ATTRIBUTE_1_NAME=\"$TEST_ELEMENT_ATTRIBUTE_1_VALUE\" $TEST_ELEMENT_ATTRIBUTE_2_NAME=\"$TEST_ELEMENT_ATTRIBUTE_2_VALUE\">some value</$TEST_ELEMENT_TAG>"
    XmlElement(testString).apply {
        require(tag == TEST_ELEMENT_TAG)
        require(attributes[TEST_ELEMENT_ATTRIBUTE_1_NAME] == TEST_ELEMENT_ATTRIBUTE_1_VALUE)
        require(attributes[TEST_ELEMENT_ATTRIBUTE_2_NAME] == TEST_ELEMENT_ATTRIBUTE_2_VALUE)
    }
}