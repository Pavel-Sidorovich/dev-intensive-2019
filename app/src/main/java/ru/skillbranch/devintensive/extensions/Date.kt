package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val different = (date.time - this.time)
    return if (different >= 0) {
        when (different) {
            in 0L * SECOND..1L * SECOND -> "только что"
            in 1L * SECOND..45L * SECOND -> "несколько секунд назад"
            in 45L * SECOND..75L * SECOND -> "минуту назад"
            in 75L * SECOND..45L * MINUTE -> "${TimeUnits.MINUTE.plural(different / MINUTE)} назад"//"${different / MINUTE} ${getNumForm("минуту;минуты;минут" , different / MINUTE)} назад"
            in 45L * MINUTE..75L * MINUTE -> "час назад"
            in 75L * MINUTE..22L * HOUR -> "${TimeUnits.HOUR.plural(different / HOUR)} назад"//"${different / HOUR} ${getNumForm("час;часа;часов" , different / HOUR)} назад"
            in 22L * HOUR..26L * HOUR -> "день назад"
            in 26L * HOUR..360L * DAY -> "${TimeUnits.DAY.plural(different / DAY)} назад"//"${different / DAY} ${getNumForm("день;дня;дней" , different / DAY)} назад"
            else -> "более года назад"
        }
    } else {
        when (val absDifferent = abs(different)) {
            in 0L * SECOND..1L * SECOND -> "только что"
            in 1L * SECOND..45L * SECOND -> "через несколько секунд"
            in 45L * SECOND..75L * SECOND -> "через минуту"
            in 75L * SECOND..45L * MINUTE -> "через ${TimeUnits.MINUTE.plural(absDifferent / MINUTE)}"//${absDifferent / MINUTE} ${getNumForm("минуту;минуты;минут" , absDifferent / MINUTE)}"
            in 45L * MINUTE..75L * MINUTE -> "через час"
            in 75L * MINUTE..22L * HOUR -> "через ${TimeUnits.HOUR.plural(absDifferent / HOUR)}"//${absDifferent / HOUR} ${getNumForm("час;часа;часов" , absDifferent / HOUR)}"
            in 22L * HOUR..26L * HOUR -> "через день"
            in 26L * HOUR..360L * DAY -> "через ${TimeUnits.DAY.plural(absDifferent / DAY)}"//${absDifferent / DAY} ${getNumForm("день;дня;дней" , absDifferent / DAY)}"
            else -> "более чем через год"
        }
    }
}

private fun getNumForm(pluralForms:String , value: Long):String {
    val forms = pluralForms.split(";")
    when (value % 10) {
        1L -> if (value % 100L != 11L)
            return forms[0]
        2L, 3L, 4L -> return if (value % 100 !in 12..14)
            forms[1]
        else forms[2]
    }
    return forms[2]
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun TimeUnits.plural(value: Long): String? {
    val forms = when(this){
        TimeUnits.SECOND -> "секунду;секунды;секунд".split(";")
        TimeUnits.MINUTE -> "минуту;минуты;минут".split(";")
        TimeUnits.HOUR -> "час;часа;часов".split(";")
        TimeUnits.DAY -> "день;дня;дней".split(";")
    }
    when (value % 10) {
        1L -> if (value % 100L != 11L)
            return "$value ${forms[0]}"
        2L, 3L, 4L -> return if (value % 100 !in 12..14)
            "$value ${forms[1]}"
        else "$value ${forms[2]}"
    }
    return "$value ${forms[2]}"
}