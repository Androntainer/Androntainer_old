package io.androntainer.fragment.settings

import android.os.Build
import android.os.Bundle
import androidx.preference.*
import io.androntainer.R
import io.androntainer.application.app.anyLauncher
import io.androntainer.application.app.defaultLauncher
import io.androntainer.application.app.taskbarSettings


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        val dynamic: SwitchPreference? = findPreference("dynamic_colors")
        val about: Preference? = findPreference("about")
        val taskbarCategory: PreferenceCategory? = findPreference("libtaskbar")
        val desktop: SwitchPreference? = findPreference("desktop")
        val config: Preference? = findPreference("config_desktop")
        val taskbar: Preference? = findPreference("taskbar")
        val fixed: Preference? = findPreference("fixed_play")
        val default: Preference? = findPreference("default_launcher")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            dynamic?.isChecked = sharedPreferences.getBoolean("developer", false)
            dynamic?.isEnabled = sharedPreferences.getBoolean("developer", false)
        }
        about?.setOnPreferenceClickListener {
            // 跳转关于页面
            true
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            taskbarCategory?.isEnabled = sharedPreferences.getBoolean("developer", false)
            desktop?.isChecked = sharedPreferences.getBoolean("developer", false)
        }
        config?.setOnPreferenceClickListener {
            // 跳转权限配置界面
            true
        }
        taskbar?.setOnPreferenceClickListener {
            taskbarSettings(requireActivity())
            true
        }
        fixed?.setOnPreferenceClickListener {
            anyLauncher(requireActivity())
            true
        }
        default?.setOnPreferenceClickListener {
            defaultLauncher(requireActivity())
            true
        }
    }
}