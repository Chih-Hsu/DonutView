package com.example.customview.ext

import android.content.res.Resources
import kotlin.math.roundToInt

fun Int.toPx(): Int {
    Resources.getSystem().displayMetrics.densityDpi
    return (Resources.getSystem().displayMetrics.density * this).roundToInt()
}

fun Int.toDp(): Int {
    return (Resources.getSystem().displayMetrics.densityDpi * this)
}