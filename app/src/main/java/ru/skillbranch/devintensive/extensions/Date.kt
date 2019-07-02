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
//    TODO("not implemented")
    var diff: Long = date.time.minus(this.time)
    val days = abs(diff / DAY)
    val hours = abs(diff / HOUR)
    val minutes = abs(diff / MINUTE)
    val seconds = abs(diff / SECOND)

    if (diff > 0) {
        return when (days) {
            0L, 1L -> {
                when (hours) {
                    0L, 1L -> {
                        when (minutes) {
                            0L, 1L -> {
                                when (seconds) {
                                    0L, 1L -> "только что"
                                    in 2L..45L -> "несколько секунд назад"
                                    in 46L..75L -> "минуту назад"
                                    else -> "1 минуту назад"
                                }
                            }
                            21L, 31L, 41L, 51L -> "$minutes минуту назад"
                            in 2L..4L, in 22..24, in 32..34, in 42..44 -> "$minutes минуты назад"
                            in 46L..75L -> "час назад"
                            in 75L..119L -> "1 час назад"
                            else -> "$minutes минут назад"
                        }
                    }
                    21L -> "$hours час назад"
                    in 2L..4L, 22L -> "$hours часа назад"
                    in 23L..26L -> "день назад"
                    in 27L..49L -> "1 день назад"
                    else -> "$hours часов назад"
                }
            }
            in 5L..20L -> "$days дней назад"
            in 361L..Long.MAX_VALUE -> "более года назад"
            else -> {
                when (days % 100) {
                    11L -> "$days дней назад"
                    else ->
                        when (days % 10) {
                            1L -> "$days день назад"
                            in 2L..4L -> "$days дня назад"
                            else -> "$days дней назад"
                        }
                }
            }
        }
    } else {
        return when (days) {
            0L, 1L -> {
                when (hours) {
                    0L, 1L -> {
                        when (minutes) {
                            0L, 1L -> {
                                when (seconds) {
                                    0L, 1L -> "только что"
                                    in 2L..45L -> "через несколько секунд"
                                    in 46L..75L -> "через минуту"
                                    else -> "через 1 минуту"
                                }
                            }
                            21L, 31L, 41L, 51L -> "через $minutes минуту"
                            in 2L..4L, in 22..24, in 32..34, in 42..44 -> "через $minutes минуты"
                            in 46L..75L -> "через час"
                            in 75L..119L -> "через 1 час"
                            else -> "через $minutes минут"
                        }
                    }
                    21L -> "через $hours час"
                    in 2L..4L, 22L -> "через $hours часа"
                    in 23L..26L -> "через день"
                    in 27L..49L -> "через 1 день"
                    else -> "через $hours часов"
                }
            }
            in 5L..20L -> "через $days дней"
            in 361L..Long.MAX_VALUE -> "более чем через год"
            else -> {
                when (days % 100) {
                    11L -> "через $days дней"
                    else ->
                        when (days % 10) {
                            1L -> "через $days день"
                            in 2L..4L -> "через $days дня"
                            else -> "через $days дней"
                        }
                }
            }
        }
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}