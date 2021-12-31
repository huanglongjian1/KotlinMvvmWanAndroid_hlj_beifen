package com.kotlin.mvvm.ext

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.App
import com.tencent.mmkv.MMKV

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/8 16:43
 */
private const val THEME = "theme"
private const val NAV_BAR = "nav_bar"
private const val NAV_BAR_COLOR = "nav_bar_color"
private const val KEY_NIGHT_MODE = "key_night_mode"
private const val SAVE_NIGHT_MODE = "save_night_mode"

fun setAppThemeColor(theme: Int) {
    MMKV.defaultMMKV().putInt(THEME, theme)
}

fun getAppThemeColor() = MMKV.defaultMMKV().getInt(THEME, ContextCompat.getColor(App.CONTEXT, R.color.colorPrimary))

fun setNightMode(nightMode: Boolean) {
    MMKV.defaultMMKV().putBoolean(KEY_NIGHT_MODE, nightMode)
}

fun getNightMode() = MMKV.defaultMMKV().getBoolean(KEY_NIGHT_MODE, false)

fun setNavBar(navBar: Boolean) {
    MMKV.defaultMMKV().putBoolean(NAV_BAR, navBar)
}

fun getNavBar() = MMKV.defaultMMKV().getBoolean(NAV_BAR, true)

fun setNavBarColor(isNavBarColor: Boolean){
    MMKV.defaultMMKV().putBoolean(NAV_BAR_COLOR, isNavBarColor)
}

fun getNavBarColor() = MMKV.defaultMMKV().getBoolean(NAV_BAR_COLOR, false)

fun saveLastNightMode(theme: Int) {
    MMKV.defaultMMKV().putInt(SAVE_NIGHT_MODE, theme)
}

fun getLastNightMode() = MMKV.defaultMMKV().getInt(SAVE_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
