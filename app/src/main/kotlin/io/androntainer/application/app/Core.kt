package io.androntainer.application.app

import android.annotation.SuppressLint
import android.app.Application
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.preference.PreferenceManager
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.androntainer.R
import io.androntainer.application.fixedplay.Launcher
import io.androntainer.ui.activity.ActivityMain


/**
 * init
 */

fun initDynamicColors(application: Application){
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (sharedPreferences.getBoolean("dynamic_colors", true)){
            DynamicColors.applyToActivitiesIfAvailable(application)
        }
    }
}

fun initDialogX(application: Application){
    DialogX.init(application)
    DialogX.globalStyle = IOSStyle()
    DialogX.globalTheme = DialogX.THEME.AUTO
    DialogX.autoShowInputKeyboard = true
    DialogX.onlyOnePopTip = false
    DialogX.cancelable = true
    DialogX.cancelableTipDialog = false
    DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
    DialogX.autoRunOnUIThread = false
    DialogX.useHaptic = true
}

fun initTaskbar(application: Application){
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        if (sharedPreferences.getBoolean("desktop", true)){
            Taskbar.setEnabled(application, true)
        } else {
            Taskbar.setEnabled(application, false)
        }
    } else {
        Taskbar.setEnabled(application, false)
    }
}

@Composable
fun MainActivityContentView(
    title: String,
    targetAppName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,

    //content: @Composable (PaddingValues) -> Unit,
){
    ActivityMain(
        title = title,
        targetAppName = targetAppName,
        targetAppPackageName = "",
        targetAppDescription = "",
        targetAppVersionName = "",
        NavigationOnClick = NavigationOnClick,
        MenuOnClick = MenuOnClick,
        SearchOnClick = SearchOnClick,
        SheetOnClick = SheetOnClick,
        AppsOnClick = AppsOnClick,
        SelectOnClick = SelectOnClick
    )
}

@SuppressLint("MissingPermission")
fun wallpaper(context: Context?): Drawable? {
    val wallpaperManager = WallpaperManager.getInstance(context)
    return wallpaperManager.drawable
}

/**
 * Settings
 */

fun taskbarSettings(context: Context){
    Taskbar.openSettings(context, "桌面模式设置", R.style.Theme_Taskbar)
}

fun defaultLauncher(context: Context){
    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun selectLauncher(context: Context){
    context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
}

fun anyLauncher(context: Context){
    val intent = Intent(context, io.androntainer.application.fixedplay.Settings::class.java)
    context.startActivity(intent)
}

fun startLauncher(context: Context){
    val intent = Intent(context, Launcher::class.java)
    context.startActivity(intent)
}