package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String {
    var result: String = this.trim()
    val newCount = count
    if (result.length > newCount) {
        result = result.substring(0, newCount)
        result = result.trimEnd().plus("...")
    }
    return result
}

fun String.stripHtml(): String {
    return this.replace("<[^<>]+>".toRegex(), "")    //Remove html tags
        .replace("&[^&;]+;".toRegex(), "")           //Remove html escape sequences
        .replace("[\\n&'\"><}]".toRegex(), "")      //Remove & <> '" \n
        .replace(" +".toRegex(), " ")            //Remove duplicate spaces
}

fun String.short128(): String? {
    return if (this.length > 128) {
        this.substring(0, 127)
    } else {
        this
    }
}