package com.lukeshay.discord.utils

fun <T> List<T>.toJSONString(
    newLines: Boolean = false
): String {
    val sb = StringBuilder()

    sb.append("[")

    var first = true

    forEach { i ->
        if (first) {
            first = false
        } else {
            sb.append(", ")
        }

        if (newLines) {
            sb.appendLine()
        }

        sb.append(
            if (newLines) {
                "    "
            } else {
                " "
            }
        )
        sb.append("\"$i\"")
    }

    if (newLines) {
        sb.appendLine()
    }

    sb.append("]")

    return sb.toString()
}