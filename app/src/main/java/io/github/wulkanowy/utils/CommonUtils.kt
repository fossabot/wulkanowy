package io.github.wulkanowy.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.customtabs.CustomTabsIntent
import io.github.wulkanowy.R
import io.github.wulkanowy.data.db.dao.entities.AttendanceType

fun openInternalBrowserViewer(activity: Activity, url: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(activity.resources.getColor(R.color.colorPrimary))
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(activity, Uri.parse(url))
}

fun colorHexToColorName(hexColor: String): Int {
    return when (hexColor) {
        "000000" -> R.string.color_black_text
        "F04C4C" -> R.string.color_red_text
        "20A4F7" -> R.string.color_blue_text
        "6ECD07" -> R.string.color_green_text
        else -> R.string.noColor_text
    }
}

@ColorInt
fun getThemeAttrColor(context: Context, @AttrRes colorAttr: Int): Int {
    val array = context.obtainStyledAttributes(null, intArrayOf(colorAttr))
    try {
        return array.getColor(0, 0)
    } finally {
        array.recycle()
    }
}

/**
 * [UONET+ - Zasady tworzenia podsumowań liczb uczniów obecnych i nieobecnych w tabeli frekwencji](https://www.vulcan.edu.pl/vulcang_files/user/AABW/AABW-PDF/uonetplus/uonetplus_Frekwencja-liczby-obecnych-nieobecnych.pdf)
 */
fun calculateAttendanceFromTypes(types: List<AttendanceType>): Double {
    var present = 0.0
    var absence = 0.0

    types.forEach {
        when(it.name) {
            "Obecność" -> present += it.value
            "Nieobecność nieusprawiedliwiona" -> absence += it.value
            "Nieobecność usprawiedliwiona" -> absence += it.value
            "Nieobecność z przyczyn szkolnych" -> present += it.value
            "Spóźnienie nieusprawiedliwione" -> present += it.value
            "Spóźnienie usprawiedliwione" -> present += it.value
        }
    }

    return (present / (present + absence)) * 100
}
