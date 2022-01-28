package com.kotlin.mvvm.ui.setting

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivitySettingBinding
import com.kotlin.mvvm.ext.*

class SettingActivity : BaseActivity(), ColorChooserDialog.ColorCallback {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<SettingViewModel>()

    override fun getContentView() = binding.root

    @SuppressLint("SetTextI18n")
    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        setThemeColor()
        supportActionBar?.title = StringUtils.getString(R.string.action_setting)
        binding.checkbox.isChecked = getNavBar()
        binding.tvVersion.setTextColor(mAppThemeColor)
        binding.tvVersionText.text =
            StringUtils.getString(R.string.current_version) + "  " + AppUtils.getAppVersionName()
        binding.llTheme.onClick {
            ColorChooserDialog.Builder(this, R.string.choose_theme_color)
                .backButton(R.string.back)
                .cancelButton(R.string.cancel)
                .doneButton(R.string.done)
                .customButton(R.string.custom)
                .presetsButton(R.string.back)
                .allowUserColorInputAlpha(false)
                .show(this)
        }
        binding.llDarkMode.onClick {
            if (getNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setNightMode(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setNightMode(true)
            }
            initThemeColor()
            initData()
        }
        binding.checkbox.onClick {
            setNavBar(binding.checkbox.isChecked)
            setNavBarColor(true)
        }
        binding.tvLogout.onClick { mViewModel.logout() }
        mViewModel.handlerCode.observe(this) {
            saveUser("")
            CookieClass.clearCookie()
            binding.tvLogout.invisible()
            finish()
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
        binding.tvDarkMode.text = if (getNightMode()) {
            StringUtils.getString(R.string.standard_mode)
        } else {
            StringUtils.getString(R.string.dark_mode)
        }
    }

    private fun setThemeColor(appThemeColor: Int = mAppThemeColor) {
        val imageColor = if (getNightMode()) {
            appThemeColor
        } else {
            if (mAppThemeColor == Color.WHITE) {
                ContextCompat.getColor(this, R.color.Grey200)
            } else {
                mAppThemeColor
            }
        }
        binding.viewTheme.setImageDrawable(ColorDrawable(imageColor))
        if (isLogin()){
            binding.tvLogout.setBackgroundColor(imageColor)
            binding.tvLogout.visible()
        } else {
            binding.tvLogout.invisible()
        }
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if (!dialog.isAccentMode) {
            setAppThemeColor(selectedColor)
            setNavBarColor(true)
        }
        initThemeColor()
        setThemeColor(selectedColor)
        initData()
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {

    }
}