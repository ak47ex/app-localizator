package com.suenara.localizator

import com.suenara.localizator.stringres.xml.XmlFileStringRes
import com.suenara.localizator.stringres.xml.XmlResFileWriter
import java.io.File

private const val SOURCE_ARGUMENT = "-source"
private const val DESTINATION_ARGUMENT = "-destination"

fun main(args: Array<String>) {
    val argsMap = args.mapNotNull { arg ->
        arg.indexOfFirst { it == '=' }.takeIf { it > 0 }?.let {
            arg.substring(0, it) to arg.substring(it + 1)
        }
    }.toMap()

    val source = argsMap[SOURCE_ARGUMENT]?.let { File(it) }
    val dest = argsMap[DESTINATION_ARGUMENT]?.let { File(it) }

    require(source != null && source.isFile) { "Invalid source ${argsMap[SOURCE_ARGUMENT]}!" }
    require(dest != null && dest.isFile) { "Invalid destination ${argsMap[DESTINATION_ARGUMENT]}!" }

    MergeStringRes(XmlFileStringRes(source), XmlFileStringRes(dest), XmlResFileWriter(dest)).merge()
}