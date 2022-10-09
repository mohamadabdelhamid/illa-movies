package com.mabdelhamid.illamovies.util.extension

import android.app.Activity
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.changeBarsColor(@ColorRes color: Int, isLightBars: Boolean = false, view: View) {
    with(window) {
        statusBarColor = ContextCompat.getColor(this@changeBarsColor, color)
        WindowInsetsControllerCompat(this, view).isAppearanceLightStatusBars = isLightBars
        navigationBarColor = ContextCompat.getColor(this@changeBarsColor, color)
        WindowInsetsControllerCompat(this, view).isAppearanceLightNavigationBars = isLightBars
    }
}
