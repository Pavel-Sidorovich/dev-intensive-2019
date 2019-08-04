package ru.skillbranch.devintensive.extensions

import android.content.res.Resources


fun Int.dpToPx(resources: Resources): Int{
    return (this * resources.displayMetrics.density + 0.5f).toInt()
}

fun Int.pxToDp(resources: Resources): Int{
    return (this / resources.displayMetrics.density + 0.5f).toInt()
}