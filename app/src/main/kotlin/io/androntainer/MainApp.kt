package io.androntainer

import android.annotation.SuppressLint
import android.app.Application
import android.app.Service
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.*
import com.android.vending.billing.IInAppBillingService
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.DynamicColors
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.navigation.NavigationView
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.androntainer.application.base.BaseActivity
import io.androntainer.application.fixedplay.FixedPlay
import io.androntainer.databinding.ActivityMainBinding
import io.androntainer.databinding.DialogLicenseBinding
import io.androntainer.databinding.SheetMainBinding
import io.androntainer.ui.activity.ActivityMain
import io.androntainer.ui.values.AndrontainerTheme
import kotlin.math.hypot


/**
 * Androntainer Project
 * Copyright (c) 2022 wyq0918dev.
 */

abstract class AndrontainerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSdk()
    }

    abstract fun initSdk()
}

abstract class AndrontainerActivity : BaseActivity() {

    override fun init() {

    }

    override fun contentView() {

    }

}

class Androntainer : AndrontainerApplication() {

    override fun initSdk() {
        initDynamicColors(this@Androntainer)
        initDialogX(this@Androntainer)
        initTaskbar(this@Androntainer)
    }
}

class MainActivity : AppCompatActivity(), Runnable {

    private lateinit var binding0: ActivityMainBinding
    private lateinit var binding1: DialogLicenseBinding
    private lateinit var binding2: SheetMainBinding
    private lateinit var context: Context
    private lateinit var toolbar: MaterialToolbar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bundle: Bundle
    private var title: String = "Androntainer"

    private var targetAppName: String by mutableStateOf("unknown")
    private var targetPackageName: String by mutableStateOf("unknown")
    private var targetDescription: String by mutableStateOf("unknown")
    private var android: String = "unknown"
    private var app = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding0 = ActivityMainBinding.inflate(layoutInflater)
        binding1 = DialogLicenseBinding.inflate(layoutInflater)
        binding2 = SheetMainBinding.inflate(layoutInflater)

        toolbar = binding2.toolbar
        setContentView(binding0.root)
        setSupportActionBar(toolbar)
        postAnim()

        init()
    }

    /**
     * Splash animation
     */

    override fun run() {
        val logo: AppCompatImageView = binding0.logo
        val greeting: ComposeView = binding0.greeting
        val cx = logo.x + logo.width / 2f
        val cy = logo.y + logo.height / 2f
        val startRadius = hypot(logo.width.toFloat(), logo.height.toFloat())
        val endRadius = hypot(greeting.width.toFloat(), greeting.height.toFloat())
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(greeting, cx.toInt(), cy.toInt(), startRadius, endRadius)
            .setDuration(800)
        logo.animate()
            .alpha(0f)
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(600)
            .withEndAction {
                logo.visibility = View.GONE
            }
            .withStartAction {
                greeting.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    private fun postAnim() {
        binding0.greeting.visibility = View.INVISIBLE
        binding0.greeting.postDelayed(this, 200)
    }

    /**
     * Main Loading Task
     */

    private fun init() {
        initContext()
        initServices()
        initData()
        initStatusBar()
        initUI()
        initBundle()
    }

    /**
     * Loading Context
     */

    private fun initContext() {
        context = this@MainActivity
    }

    /**
     * Loading background services
     */

    private fun initServices() {
        val intent = Intent(context, InAppBillingService::class.java)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {

        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }

    /**
     * Loading Data
     */

    private fun initData() {
        title = getString(R.string.app_name)
        targetAppName = "App"
        android = getAndroidVersion()

    }

    /**
     * Loading system StatusBar style
     */

    @Suppress("DEPRECATION")
    private fun initStatusBar() {
        val option =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        val decorView = window.decorView
        val visibility: Int = decorView.systemUiVisibility
        decorView.systemUiVisibility = visibility or option
    }

    /**
     * Loading App Ui
     */

    private fun initUI() {
        // Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // Binding View
        val greeting: ComposeView = binding0.greeting
        val navView: NavigationView = binding0.navView
        val navHostFragment =
            (supportFragmentManager.findFragmentById(binding2.navHostFragmentContentMain.id) as NavHostFragment?)!!
        // ComposeView layout
        greeting.apply {
            setContent {
                MdcTheme {
                    Layout()
                }
            }
        }
        // Navigation
        drawerLayout = binding0.drawerLayout
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_settings,
                R.id.nav_dashboard
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Navigation Fragment host BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
        bottomSheetDialog.window?.setDimAmount(0f)
        bottomSheetDialog.setContentView(binding2.root)



       // fullScreenDialog.show()
    }

    private fun initBundle() {
        bundle = Bundle()
    }

    /**
     * Jetpack Compose Layout
     */

    @Composable
    private fun Layout() {
        MainActivityContentView(
            title = title,
            targetAppName = targetAppName,
            NavigationOnClick = {
                drawer()
            },
            MenuOnClick = {
                optionsMenu()
            },
            SearchOnClick = {

            },
            SheetOnClick = {
                sheet()
            },
            AppsOnClick = {
                apps()
            },
            SelectOnClick = {
                select()
            }
        )
    }

    private fun drawer() {
        if (drawerLayout.isOpen) {
            drawerLayout.close()
        } else {
            drawerLayout.open()
        }
    }

    private fun optionsMenu() {
        sheet()
        toolbar.showOverflowMenu()
        //openOptionsMenu()
    }

    private fun sheet() {
        if (bottomSheetDialog.isShowing) {
            bottomSheetDialog.hide()
        } else {
            bottomSheetDialog.show()
        }


    }

    private fun apps() {
        sheet()
        navigate(R.id.nav_settings)//这里要改成apps页面
    }

    private fun select() {

    }

    private fun getAndroidVersion(): String {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.LOLLIPOP -> "Android Lollipop 5.0"
            Build.VERSION_CODES.LOLLIPOP_MR1 -> "Android Lollipop 5.1"
            Build.VERSION_CODES.M -> "Android Marshmallow 6.0"
            Build.VERSION_CODES.N -> "Android Nougat 7.0"
            Build.VERSION_CODES.N_MR1 -> "Android Nougat 7.1"
            Build.VERSION_CODES.O -> "Android Oreo 8.0"
            Build.VERSION_CODES.O_MR1 -> "Android Oreo 8.1"
            Build.VERSION_CODES.P -> "Android Pie 9.0"
            Build.VERSION_CODES.Q -> "Android Q 10.0"
            Build.VERSION_CODES.R -> "Android RedVelvetCake 11.0"
            Build.VERSION_CODES.S -> "Android SnowCone 12.0"
            Build.VERSION_CODES.S_V2 -> "Android SnowCone V2 12.1"
            Build.VERSION_CODES.TIRAMISU -> "Android Tiramisu 13.0"
            else -> "unknown"
        }
    }

    private fun navigate(resId: Int) {
        navController.navigate(resId, bundle)//这里要改成apps页面
    }

    /**
     * Bottom Sheet Dialog full screen
     */

    override fun onStart() {
        super.onStart()
        val view: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        val behavior = BottomSheetBehavior.from(view)
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**
     * Create OptionsMenu
     */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * Options menu click
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return super.onOptionsItemSelected(item)
    }

    /**
     * Support navigate up
     */

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Jetpack Compose layout preview
     */

    @Preview
    @Composable
    fun ActivityMainPreview() {
        AndrontainerTheme {
            Layout()
        }
    }

    /**
     * Remove content call back
     */

    override fun onDestroy() {
        binding0.greeting.removeCallbacks(this)
        super.onDestroy()
    }
}

class Launcher : FixedPlay()

@RequiresApi(Build.VERSION_CODES.M)
open class FakeSignature : AppCompatActivity() {
    protected fun main(code: Int) {
        if (check(this@FakeSignature)) {
            setResult(RESULT_OK)
        } else {
            requires(this@FakeSignature, code)
        }
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 3 && grantResults.size == 3) {
            setResult(if (grantResults[0] == PackageManager.PERMISSION_GRANTED) RESULT_OK else RESULT_CANCELED)
            finish()
        }
    }

    companion object {

        fun check(activity: AppCompatActivity): Boolean {
            return activity.checkSelfPermission("android.permission.FAKE_PACKAGE_SIGNATURE") == PackageManager.PERMISSION_GRANTED
        }

        fun requires(activity: AppCompatActivity, code: Int) {
            activity.requestPermissions(arrayOf("android.permission.FAKE_PACKAGE_SIGNATURE"), code)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
class Google : FakeSignature() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        main(1)
    }
}

class InAppBillingService : Service() {

    private val tag = "FakeInAppStore"

    private val mInAppBillingService: IInAppBillingService.Stub =
        object : IInAppBillingService.Stub() {
            override fun isBillingSupported(
                apiVersion: Int,
                packageName: String,
                type: String
            ): Int {
                return isBillingSupportedV7(apiVersion, packageName, type, Bundle())
            }

            override fun getSkuDetails(
                apiVersion: Int,
                packageName: String,
                type: String,
                skusBundle: Bundle
            ): Bundle {
                return getSkuDetailsV10(apiVersion, packageName, type, skusBundle, Bundle())
            }

            override fun getBuyIntent(
                apiVersion: Int,
                packageName: String,
                sku: String,
                type: String,
                developerPayload: String
            ): Bundle {
                return getBuyIntentV6(
                    apiVersion,
                    packageName,
                    sku,
                    type,
                    developerPayload,
                    Bundle()
                )
            }

            override fun getPurchases(
                apiVersion: Int,
                packageName: String,
                type: String,
                continuationToken: String
            ): Bundle {
                return getPurchasesV6(apiVersion, packageName, type, continuationToken, Bundle())
            }

            override fun consumePurchase(
                apiVersion: Int,
                packageName: String,
                purchaseToken: String
            ): Int {
                return consumePurchaseV9(
                    apiVersion,
                    packageName,
                    purchaseToken,
                    Bundle()
                ).getInt("RESPONSE_CODE", 8)
            }

            override fun getBuyIntentToReplaceSkus(
                apiVersion: Int,
                packageName: String,
                oldSkus: List<String>,
                newSku: String,
                type: String,
                developerPayload: String
            ): Bundle {
                Log.d(
                    tag,
                    "getBuyIntentToReplaceSkus($apiVersion, $packageName, $newSku, $type, $developerPayload)"
                )
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 4)
                return data
            }

            override fun getBuyIntentV6(
                apiVersion: Int,
                packageName: String,
                sku: String,
                type: String,
                developerPayload: String,
                extras: Bundle
            ): Bundle {
                Log.d(
                    tag,
                    "getBuyIntent($apiVersion, $packageName, $sku, $type, $developerPayload)"
                )
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 4)
                return data
            }

            override fun getPurchasesV6(
                apiVersion: Int,
                packageName: String,
                type: String,
                continuationToken: String,
                extras: Bundle
            ): Bundle {
                return getPurchasesV9(apiVersion, packageName, type, continuationToken, extras)
            }

            override fun isBillingSupportedV7(
                apiVersion: Int,
                packageName: String,
                type: String,
                extras: Bundle
            ): Int {
                Log.d(
                    tag,
                    "isBillingSupported($apiVersion, $packageName, $type)"
                )
                return 0
            }

            override fun getPurchasesV9(
                apiVersion: Int,
                packageName: String,
                type: String,
                continuationToken: String,
                extras: Bundle
            ): Bundle {
                Log.d(
                    tag,
                    "getPurchases($apiVersion, $packageName, $type, $continuationToken)"
                )
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 0)
                data.putStringArrayList("INAPP_PURCHASE_ITEM_LIST", ArrayList())
                data.putStringArrayList("INAPP_PURCHASE_DATA_LIST", ArrayList())
                data.putStringArrayList("INAPP_DATA_SIGNATURE_LIST", ArrayList())
                return data
            }

            override fun consumePurchaseV9(
                apiVersion: Int,
                packageName: String,
                purchaseToken: String,
                extras: Bundle
            ): Bundle {
                Log.d(
                    tag,
                    "consumePurchase($apiVersion, $packageName, $purchaseToken)"
                )
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 8)
                return data
            }

            override fun getPriceChangeConfirmationIntent(
                apiVersion: Int,
                packageName: String,
                sku: String,
                type: String,
                extras: Bundle
            ): Bundle {
                Log.d(
                    tag,
                    "getPriceChangeConfirmationIntent($apiVersion, $packageName, $sku, $type)"
                )
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 4)
                return data
            }

            override fun getSkuDetailsV10(
                apiVersion: Int,
                packageName: String,
                type: String,
                skuBundle: Bundle,
                extras: Bundle
            ): Bundle {
                Log.d(tag, "getSkuDetails($apiVersion, $packageName, $type)")
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 0)
                data.putStringArrayList("DETAILS_LIST", ArrayList())
                return data
            }

            override fun acknowledgePurchase(
                apiVersion: Int,
                packageName: String,
                purchaseToken: String,
                extras: Bundle
            ): Bundle {
                Log.d(
                    tag,
                    "acknowledgePurchase($apiVersion, $packageName, $purchaseToken)"
                )
                val data = Bundle()
                data.putInt("RESPONSE_CODE", 8)
                return data
            }

            @Throws(RemoteException::class)
            override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
                if (super.onTransact(code, data, reply, flags)) return true
                Log.d(
                    tag,
                    "onTransact [unknown]: $code, $data, $flags"
                )
                return false
            }
        }

    override fun onBind(intent: Intent?): IBinder {
        return mInAppBillingService
    }
}

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
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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