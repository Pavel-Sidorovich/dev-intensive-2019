package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 17): String{
    var result: String = this.trimEnd()
    if(result.length > count) {
        result = result.substring(0, count)

        result = result.trimEnd().plus("...")
    }
    return result
}

fun String.stripHtml(): String{
    return this.replace("<[^<>]+>".toRegex(),"")    //Remove html tags
    .replace("[&'\"><}]".toRegex(), "")      //Remove html escape sequences such as & <> '"
    .replace(" +".toRegex(), " ")            //Remove duplicate spaces
}