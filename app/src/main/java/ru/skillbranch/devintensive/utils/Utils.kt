package ru.skillbranch.devintensive.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.util.DisplayMetrics
import androidx.annotation.AttrRes
import java.util.*
import java.util.Arrays.asList
import kotlin.math.abs


/**
 * Вспомогательные функции
 */
object Utils {

    /**
     * Разбивает полное имя на имя и фамилию, если возможно
     */
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName == "" || fullName == " " || fullName == null)
            return null to null
        val parts: List<String> = fullName.split(" ")
        var firstName: String? = null
        var lastName: String? = null
        var key = false
        for (item in parts) {
            if (!key && item != "" && item != " ") {
                firstName = item
                key = true
            } else if (key && item != "" && item != " ") {
                lastName = item
                return firstName to lastName
            }
        }
        return firstName to lastName
    }

    /**
     * Возвращает строку, написанную латинскими символами. Заменяет " " на указанный divider
     */
    fun transliteration(payload: String, divider: String = " "): String {
        return convertRU(payload.trim().replace(" ", divider))
    }

    /**
     * Возвращает инициалы пользователя
     */
    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName != null && firstName.trim() != "") {
            val trimFirstName = firstName.trim()
            if (lastName != null && lastName.trim() != "") {
                val trimLastName = lastName.trim()
                return trimFirstName[0].toUpperCase() + "" + trimLastName[0].toUpperCase()
            }
            return trimFirstName[0].toUpperCase() + ""
        } else if (lastName != null && lastName.trim() != "") {
            val trimLastName = lastName.trim()
            return trimLastName[0].toUpperCase() + ""
        } else return null
    }

    /**
     * Вспомогательная функция для transliteration
     */
    private fun convertRU(cyr: String): String {
        //Lower case letters
        var lat = cyr.replace("а".toRegex(), "a")
        lat = lat.replace("б".toRegex(), "b")
        lat = lat.replace("в".toRegex(), "v")
        lat = lat.replace("г".toRegex(), "g")
        lat = lat.replace("д".toRegex(), "d")
        lat = lat.replace("е".toRegex(), "e")
        lat = lat.replace("ё".toRegex(), "e")
        lat = lat.replace("ж".toRegex(), "zh")
        lat = lat.replace("з".toRegex(), "z")
        lat = lat.replace("и".toRegex(), "i")
        lat = lat.replace("й".toRegex(), "i")
        lat = lat.replace("к".toRegex(), "k")
        lat = lat.replace("л".toRegex(), "l")
        lat = lat.replace("м".toRegex(), "m")
        lat = lat.replace("н".toRegex(), "n")
        lat = lat.replace("о".toRegex(), "o")
        lat = lat.replace("п".toRegex(), "p")
        lat = lat.replace("р".toRegex(), "r")
        lat = lat.replace("с".toRegex(), "s")
        lat = lat.replace("т".toRegex(), "t")
        lat = lat.replace("у".toRegex(), "u")
        lat = lat.replace("ф".toRegex(), "f")
        lat = lat.replace("х".toRegex(), "h")
        lat = lat.replace("ц".toRegex(), "c")
        lat = lat.replace("ч".toRegex(), "ch")
        lat = lat.replace("ш".toRegex(), "sh")
        lat = lat.replace("щ".toRegex(), "sh'")
        lat = lat.replace("ъ".toRegex(), "")
        lat = lat.replace("ы".toRegex(), "i")
        lat = lat.replace("ь".toRegex(), "")
        lat = lat.replace("э".toRegex(), "e")
        lat = lat.replace("ю".toRegex(), "yu")
        lat = lat.replace("я".toRegex(), "ya")

        //Capital letters
        lat = lat.replace("А".toRegex(), "A")
        lat = lat.replace("Б".toRegex(), "B")
        lat = lat.replace("В".toRegex(), "V")
        lat = lat.replace("Г".toRegex(), "G")
        lat = lat.replace("Д".toRegex(), "D")
        lat = lat.replace("Е".toRegex(), "E")
        lat = lat.replace("Ё".toRegex(), "E")
        lat = lat.replace("Ж".toRegex(), "Zh")
        lat = lat.replace("З".toRegex(), "Z")
        lat = lat.replace("И".toRegex(), "I")
        lat = lat.replace("Й".toRegex(), "I")
        lat = lat.replace("К".toRegex(), "K")
        lat = lat.replace("Л".toRegex(), "L")
        lat = lat.replace("М".toRegex(), "M")
        lat = lat.replace("Н".toRegex(), "N")
        lat = lat.replace("О".toRegex(), "O")
        lat = lat.replace("П".toRegex(), "P")
        lat = lat.replace("Р".toRegex(), "R")
        lat = lat.replace("С".toRegex(), "S")
        lat = lat.replace("Т".toRegex(), "T")
        lat = lat.replace("У".toRegex(), "U")
        lat = lat.replace("Ф".toRegex(), "F")
        lat = lat.replace("Х".toRegex(), "H")
        lat = lat.replace("Ц".toRegex(), "C")
        lat = lat.replace("Ч".toRegex(), "Ch")
        lat = lat.replace("Ш".toRegex(), "Sh")
        lat = lat.replace("Щ".toRegex(), "Sh'")
        lat = lat.replace("Ъ".toRegex(), "")
        lat = lat.replace("Ы".toRegex(), "I")
        lat = lat.replace("Ь".toRegex(), "")
        lat = lat.replace("Э".toRegex(), "E")
        lat = lat.replace("Ю".toRegex(), "Yu")
        lat = lat.replace("Я".toRegex(), "Ya")


        return lat //Return latinized string
    }

    /**
     * Проверяет на валидность GitHub
     */
    fun verification(github: String) = Regex("((https://|www.|https://www.)?github.com/(?!enterprise$|features$|topics$|collections$|trending$|events$|marketplace$|pricing$|nonprofit$|customer-stories$|security$|login$|join$)[\\w\\d-_]{1,39}/?$)|").find(github)?.value == github

    /**
     * Преобразует Attr формат в Color
     */
    public fun getColorFromAttr(
        @AttrRes attrColor: Int,
        theme : Resources.Theme,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    fun getColorRandom(key: String): Int{
        val colors = ArrayList<Int>()
        colors.addAll(listOf(
            -0xbbcca, -0x16e19d, -0x63d850, -0x98c549,
            -0xc0ae4b, -0xde690d, -0xfc560c, -0xff432c,
            -0xff6978, -0xb350b0, -0x743cb6, -0x3223c7,
            -0x14c5, -0x3ef9, -0x6800, -0xa8de,
            -0x86aab8, -0x616162, -0x9f8275, -0xcccccd
        ))
        val whichColor = abs(key.hashCode()) % colors.size
        return colors[whichColor]
    }
}

