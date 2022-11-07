/**
 * Androntainer Project
 * Copyright (c) 2022 wyq0918dev.
 */
package io.androntainer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Service
import android.app.WallpaperManager
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.*
import com.android.vending.billing.IInAppBillingService
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.DynamicColors
import com.google.android.material.navigation.NavigationView
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.GlobalLifeCircleListener
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle
import io.androntainer.databinding.*
import kotlinx.coroutines.Runnable
import kotlin.math.hypot


abstract class AndrontainerApplication : BaseApp<Androntainer>() {

    override fun init() {
        setOnSDKInitializedCallBack {
            log("SDK已加载完毕")
            toast("SDK已加载完毕")
        }
    }

    override fun initSDKs() {
        super.initSDKs()
        initSdk()
    }

    abstract fun initSdk()
}

abstract class AndrontainerActivity : BaseActivity(), Runnable {

    override fun resetContentView(): View? {
        initView()
        return contentView()
    }

    override fun initViews() {
        initContext()
        initServices()
        initSystemBar()
        initUi()
        initBundle()
    }

    override fun initDatas(parameter: JumpParameter?) {
        postAnim()
        initData(parameter)
    }

    override fun setEvents() {
        setEvent()
    }

    override fun run() {
        runnable()
    }

    protected val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {

        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }

    abstract fun initView()
    abstract fun contentView(): View?
    abstract fun runnable()
    abstract fun postAnim()
    abstract fun initContext()
    abstract fun initServices()
    abstract fun initData(parameter: JumpParameter?)
    abstract fun initSystemBar()
    abstract fun initUi()
    abstract fun initBundle()
    abstract fun setEvent()
}


abstract class FixedActivity : BaseActivity() {
    override fun initViews() {

    }

    override fun initDatas(parameter: JumpParameter?) {

    }

    override fun setEvents() {

    }

}

abstract class AndrontainerFragment<me : BaseActivity> : BaseFragment<me>() {

    override fun initViews() {

    }

    override fun initDatas() {

    }

    override fun setEvents() {

    }

}

class Androntainer : AndrontainerApplication() {

    override fun initSdk() {
        //initDynamicColors(me)
        initDialogX(me)
        initTaskbar(me)
    }
}

class MainActivity : AndrontainerActivity() {

    private lateinit var binding0: ActivityMainBinding
    private lateinit var binding1: DialogLicenseBinding
    private lateinit var binding2: SheetMainBinding
    private lateinit var thisContext: Context
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
    private var androidVersion: String = "unknown"
    private var app = null

    override fun initView() {
        binding0 = ActivityMainBinding.inflate(layoutInflater)
        binding1 = DialogLicenseBinding.inflate(layoutInflater)
        binding2 = SheetMainBinding.inflate(layoutInflater)

        val toolbar = MaterialToolbar(me).apply {

            popupTheme = R.style.ThemeOverlay_Androntainer_NoActionBar_PopupOverlay
            setBackgroundColor(
                ContextCompat.getColor(
                    me,
                    R.color.purple_500
                )
            )
        }

        val logo = AppCompatImageView(me).apply {

        }

        val compose = ComposeView(me).apply {

        }

        val layout = LinearLayoutCompat(
            me
        ).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                AppBarLayout(
                    me
                ).apply {
                    setTheme(R.style.ThemeOverlay_Androntainer_NoActionBar_AppBarOverlay)
                    addView(
                        toolbar,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    )
                },
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                CoordinatorLayout(
                    me
                ).apply {
                    addView(
                        ComposeView(
                            me
                        ).apply {

                        },
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    addView(
                        AppCompatImageView(
                            me
                        ).apply {

                        },
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                },
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun contentView(): View {
        val root: View = binding0.root
        toolbar = binding2.toolbar
        setSupportActionBar(toolbar)
        return root
    }

    /**
     * Splash animation
     */

    override fun runnable() {
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

    override fun postAnim() {
        binding0.greeting.visibility = View.INVISIBLE
        binding0.greeting.postDelayed(this, 200)
    }

    /**
     * Loading Context
     */

    override fun initContext() {
        thisContext = me
    }

    /**
     * Loading background services
     */

    override fun initServices() {
        val intent = Intent(thisContext, InAppBillingService::class.java)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun initData(parameter: JumpParameter?) {
        title = getString(R.string.app_name)
        targetAppName = "App"
        androidVersion = getAndroidVersion()
    }

    /**
     * Loading system bar style
     */

    @Suppress("DEPRECATION")
    override fun initSystemBar() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val option =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        val decorView = window.decorView
        val visibility: Int = decorView.systemUiVisibility
        decorView.systemUiVisibility = visibility or option
        setDarkStatusBarTheme(true)
        setDarkNavigationBarTheme(true)
        setNavigationBarBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    /**
     * Loading App Ui
     */

    override fun initUi() {
        // Binding View
        val greeting: ComposeView = binding0.greeting
        val navView: NavigationView = binding0.navView
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
        // ComposeView layout
        greeting.apply {
            setContent {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(application)
                    AndrontainerTheme(
                        dynamicColor = sharedPreferences.getBoolean(
                            "dynamic_colors",
                            true
                        )
                    ) {
                        Layout()
                    }
                } else {
                    AndrontainerTheme(
                        dynamicColor = false
                    ) {
                        Layout()
                    }
                }
            }
        }
        // Navigation Fragment host BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(thisContext, R.style.BottomSheetDialogStyle)
        bottomSheetDialog.window?.setDimAmount(0f)
        bottomSheetDialog.setContentView(binding2.root)


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

    }

    override fun initBundle() {
        bundle = Bundle()
    }

    override fun setEvent() {
        setGlobalLifeCircleListener(
            object : GlobalLifeCircleListener() {
                override fun onCreate(me: BaseActivity, className: String) {
                    super.onCreate(me, className)

                }

                override fun onResume(me: BaseActivity, className: String) {
                    super.onResume(me, className)

                }

                override fun onPause(me: BaseActivity, className: String) {
                    super.onPause(me, className)

                }

                override fun onDestroy(me: BaseActivity, className: String) {
                    super.onDestroy(me, className)
                    binding0.greeting.removeCallbacks(this@MainActivity)
                }
            }
        )
        AppManager.setOnActivityStatusChangeListener(
            object : AppManager.OnActivityStatusChangeListener() {
                override fun onActivityCreate(activity: BaseActivity) {
                    super.onActivityCreate(activity)

                }

                override fun onActivityDestroy(activity: BaseActivity) {
                    super.onActivityDestroy(activity)

                }

                override fun onAllActivityClose() {
                    Log.e(">>>", "所有Activity已经关闭")
                }
            }
        )
    }

    /**
     * Bottom Sheet Dialog full screen
     */

    override fun onStart() {
        super.onStart()
        val view: FrameLayout =
            bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
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
            Build.VERSION_CODES.LOLLIPOP_MR1 -> "Android Lollipop MR1 5.1"
            Build.VERSION_CODES.M -> "Android Marshmallow 6.0"
            Build.VERSION_CODES.N -> "Android Nougat 7.0"
            Build.VERSION_CODES.N_MR1 -> "Android Nougat MR1 7.1"
            Build.VERSION_CODES.O -> "Android Oreo 8.0"
            Build.VERSION_CODES.O_MR1 -> "Android Oreo MR1 8.1"
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
        navController.navigate(resId, bundle)
    }


}


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

class FixedPlay : AppCompatActivity() {

    private var mPackageManager: PackageManager? = null

    private val thisPackage = AppUtils.getAppPackageName()
    private var mode: String? = "r2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPackageManager = packageManager
        go()
        finish()
    }

    @SuppressLint("WrongConstant")
    fun go() {
        val read = PreferenceManager.getDefaultSharedPreferences(this@FixedPlay)
        val app = read.getString("app", "")
        val className = read.getString("class", "")
        mode = read.getString("mode", "r2")
        if (app!!.isNotEmpty() && app != thisPackage) {
            when (mode) {
                "r2" -> {
                    Log.w("MainActivity mode2", mode!!)
                    val intent = packageManager!!.getLaunchIntentForPackage(app)
                    intent?.let {
                        startActivity(it)
                    }
                }
                "r1" -> if (className!!.length > 5) {
                    val intent = Intent()
                    intent.setClassName(app, className)
                    startActivity(intent)
                } else {
                    val intent: Intent? = packageManager!!.getLaunchIntentForPackage(app)
                    intent!!.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    this.startActivity(intent)
                }
            }
        } else {
            val intent = Intent(this@FixedPlay, FixedSettings::class.java)
            startActivity(intent)
        }
    }
}

class FixedSettings : AppCompatActivity() {

    private var mPackageManager: PackageManager? = null
    private var _binding: ActivityLauncherSettingsBinding? = null
    private val binding get() = _binding!!
    private var _mode = ""
    private var packageName: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // packageManager
        mPackageManager = packageManager
        // Intent
        val intent = intent
        // Layout
        _binding = ActivityLauncherSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Views
        val button1: AppCompatButton = binding.button1
        val button2: AppCompatButton = binding.button2
        packageName = binding.textPakageName
        val toolbar: Toolbar = binding.toolbar
        // Click
        button1.setOnClickListener {
            selectLauncher(this@FixedSettings)
        }
        button2.setOnClickListener {
            button2()
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
        // Update Layout
        go()
        // Select App
        val bundle = intent.extras
        val setting = bundle?.getString("settings")
        if (setting != null) {
            if (setting == "select_app") {
                button2()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SdCardPath")
    private fun button2() {
        val btf = getString(R.string.btf)
        val act = getString(R.string.act)
        val mode = getString(R.string.mode)
        val items3 = arrayOf(btf, act)
        val alertDialog3: AlertDialog = AlertDialog.Builder(this@FixedSettings)
            .setTitle(mode)
            .setIcon(R.drawable.ic_baseline_androntainer_plat_logo_24)
            .setItems(items3) { _: DialogInterface?, i: Int ->
                _mode = ""
                val select = Intent(this@FixedSettings, SelectOne::class.java)
                when (i) {
                    0 -> {
                        _mode = "r2"
                        select.putExtra("_mode", _mode)
                        startActivity(select)
                    }
                    1 -> {
                        _mode = "r1"
                        select.putExtra("_mode", _mode)
                        startActivity(select)
                    }
                }
            }
            .create()
        alertDialog3.show()
    }

    public override fun onResume() {
        super.onResume()
        go()
    }

    @SuppressLint("SetTextI18n")
    fun go() {
        val read = PreferenceManager.getDefaultSharedPreferences(this@FixedSettings)
        val app = read.getString("app", "")
        if (app!!.isEmpty()) return
        val label = read.getString("label", "")
        val uri = read.getString("uri", "")
        val className = read.getString("class", "")
        val icon: Drawable
        try {
            val appInfo = packageManager!!.getApplicationInfo(app, 0)
            icon = appInfo.loadIcon(packageManager)
            runOnUiThread {
                packageName!!.text = label
                binding.applistItem.itemImg.setImageDrawable(icon)
                binding.applistItem.itemText.text = app
                binding.applistItem.itemPackageName.text = """
                                    $uri
                                    $className
                                    """.trimIndent()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class SelectOne : AppCompatActivity() {

    private var _binding: ActivityApplistBinding? = null
    private val binding get() = _binding!!
    private val list: MutableList<Item?> = ArrayList()
    private var listView: ListView? = null
    private var progressBar: ProgressBar? = null
    private var _mode: String? = null
    private var _uri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityApplistBinding.inflate(layoutInflater)

        setContentView(binding.root)
        progressBar = binding.progressBar
        val intent = intent
        val bundle = intent.extras
        _mode = bundle?.getString("_mode")
        _uri = bundle?.getString("_uri")
        Thread {
            loadAllApps(makeIntent())
            val itemAdapter = ItemAdapter(this@SelectOne, R.layout.item_applist, list)
            try {
                itemAdapter.setMode(_mode!!)
                itemAdapter.setUri(_uri!!)
            } catch (e: Exception) {

            }
            runOnUiThread {
                listView = binding.listview
                listView!!.adapter = itemAdapter
                progressBar!!.visibility = View.GONE
            }
        }.start()
        val toolbar: Toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun loadAllApps(intent: Intent?) {
        val mApps: MutableList<ResolveInfo>
        mApps = ArrayList()
        try {
            mApps.addAll(this.packageManager.queryIntentActivities(intent!!, 0))
            val pm = packageManager
            for (r in mApps) {
                val item = Item(
                    r.loadLabel(pm).toString(),
                    r.activityInfo.packageName,
                    r.activityInfo.name,
                    r.loadIcon(pm)
                )
                list.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant, UseSwitchCompatOrMaterialCode")
    private fun makeIntent(): Intent? {
        when (_mode) {
            "r2", "r1" -> {
                val mainIntent = Intent(Intent.ACTION_MAIN, null)
                mainIntent.addCategory(Intent.CATEGORY_HOME)
                return mainIntent
            }
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@RequiresApi(Build.VERSION_CODES.M)
class Google : FakeSignature() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        main(1)
    }
}

//class Launcher : FixedPlay()

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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
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

class Item(
    val name: String,
    val packageName: String,
    val className: String,
    val appIcon: Drawable
)

class ItemAdapter(
    context: Context?,
    private val layoutId:
    Int, list: List<Item?>?
) : ArrayAdapter<Item?>(
    context!!,
    layoutId,
    list!!
) {

    private var mode = "r2"
    private var uri = ""

    fun setMode(mode: String) {
        this.mode = mode
    }

    fun setUri(uri: String) {
        this.uri = uri
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        val packageName = item!!.packageName
        (view.findViewById<View>(R.id.item_img) as AppCompatImageView).setImageDrawable(
            item.appIcon
        )
        (view.findViewById<View>(R.id.item_text) as AppCompatTextView).text = item.name
        (view.findViewById<View>(R.id.item_packageName) as AppCompatTextView).text =
            packageName
        view.setOnClickListener {
            val pm = context.packageManager
            var intent = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                val editor =
                    PreferenceManager.getDefaultSharedPreferences(context)
                        .edit()
                editor.putString("app", packageName)
                editor.putString("label", item.name)
                editor.putString("class", item.className)
                editor.putString("uri", uri)
                editor.putString("mode", mode)
                editor.apply()
                intent =
                    Intent(context, FixedSettings::class.java)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, R.string.error_could_not_start, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }
}


fun initDynamicColors(application: Application) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (sharedPreferences.getBoolean("dynamic_colors", true)) {
            DynamicColors.applyToActivitiesIfAvailable(application)
        }
    }
}

fun initDialogX(application: Application) {
    DialogX.init(application)
    DialogX.globalStyle = MaterialYouStyle()
    DialogX.globalTheme = DialogX.THEME.AUTO
    DialogX.autoShowInputKeyboard = true
    DialogX.onlyOnePopTip = false
    DialogX.cancelable = true
    DialogX.cancelableTipDialog = false
    DialogX.bottomDialogNavbarColor = android.graphics.Color.TRANSPARENT
    DialogX.autoRunOnUIThread = false
    DialogX.useHaptic = true
}

fun initTaskbar(application: Application) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (sharedPreferences.getBoolean("desktop", true)) {
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
) {
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

fun taskbarSettings(context: Context) {
    Taskbar.openSettings(context, "桌面模式设置", R.style.Theme_Taskbar)
}

fun defaultLauncher(context: Context) {
    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun selectLauncher(context: Context) {
    context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
}

fun anyLauncher(context: Context) {
    val intent = Intent(context, FixedSettings().javaClass)
    context.startActivity(intent)
}

fun startLauncher(context: Context) {
    val intent = Intent(context, FixedPlay().javaClass)
    context.startActivity(intent)
}

const val aidlux: String = "com.aidlux"

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AndrontainerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()

            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@SuppressLint("InflateParams")
@Composable
fun WidgetClock() {
    AndroidView(
        factory = { context ->
            LayoutInflater.from(context)
                .inflate(R.layout.widget_clock, null)
        }
    )
}

@Preview
@Composable
fun WidgetClockPreview() {
    WidgetClock()
}

@Composable
fun WidgetControl(
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SelectOnClick: () -> Unit
) {
    AndroidViewBinding(
        factory = WidgetControlBinding::inflate
    ) {
        controlDrawer.setOnClickListener {
            NavigationOnClick()
        }
        controlMenu.setOnClickListener {
            MenuOnClick()
        }
        controlApps.setOnClickListener {
            AppsOnClick()
        }
        controlSearch.setOnClickListener {
            SearchOnClick()
        }
        controlSelect.setOnClickListener {
            SelectOnClick()
        }
    }
}

@Preview
@Composable
fun WidgetControlPreview() {
    WidgetControl(
        NavigationOnClick = {},
        MenuOnClick = {},
        AppsOnClick = {},
        SearchOnClick = {},
        SelectOnClick = {}
    )
}

@Composable
fun WidgetTargetApp(
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    targetAppChecked: () -> Unit,
    targetAppUnchecked: () -> Unit,
) {
    AndroidViewBinding(
        factory = WidgetTargetAppBinding::inflate
    ) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) targetAppChecked() else targetAppUnchecked()
        }
        title.text = targetAppName
        versionName.text = targetAppVersionName
        packageName.text = targetAppPackageName
        description.text = targetAppDescription
    }
}

@Preview
@Composable
fun WidgetTargetAppPreview() {
    WidgetTargetApp(
        targetAppName = "Androntainer",
        targetAppPackageName = "unknown",
        targetAppDescription = "unknown",
        targetAppVersionName = "1.0",
        targetAppChecked = {},
        targetAppUnchecked = {}
    )
}

@Composable
fun ActivityMain(
    title: String,
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
) {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                ScreenHome(
                    title = title,
                    targetAppName = targetAppName,
                    targetAppPackageName = targetAppPackageName,
                    targetAppDescription = targetAppDescription,
                    targetAppVersionName = targetAppVersionName,
                    NavigationOnClick = NavigationOnClick,
                    MenuOnClick = MenuOnClick,
                    SearchOnClick = SearchOnClick,
                    SheetOnClick = SheetOnClick,
                    AppsOnClick = AppsOnClick,
                    SelectOnClick = SelectOnClick,
                    onNavigateToApps = { navController.navigate("apps") },
                )
            }
            composable("apps") {

            }
        }

    }
}

@Preview
@Composable
fun ActivityMainPreview() {
    AndrontainerTheme {
        ActivityMain(
            title = "",
            targetAppName = "",
            targetAppPackageName = "",
            targetAppDescription = "",
            targetAppVersionName = "",
            NavigationOnClick = {},
            MenuOnClick = {},
            SearchOnClick = {},
            SheetOnClick = {},
            AppsOnClick = {},
            SelectOnClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHome(
    title: String,
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
    onNavigateToApps: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val expandedMenu = remember { mutableStateOf(false) }
    val expandedPowerButton = remember { mutableStateOf(true) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = title)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            //NavigationOnClick()

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            expandedMenu.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = expandedMenu.value,
                        onDismissRequest = {
                            expandedMenu.value = false
                        },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("分享")
                            },
                            onClick = {

                                expandedMenu.value = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text("抽屉")
                            },
                            onClick = {
//                                scope.launch {
//                                    scaffoldState.drawerState.apply {
//                                        if (isClosed) open() else close()
//                                    }
//                                }
//                                expandedMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text("更多")
                            },
                            onClick = {
                                expandedMenu.value = false
                                MenuOnClick()
                            }
                        )
                    }
                },
            )
        },
        bottomBar = {

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToApps,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
//                Image(
//                    painter = painterResource(id = R.drawable.background),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.FillBounds
//                )
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "当前时间",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WidgetClock()
                    }
                }

                Divider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "控制台",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WidgetControl(
                            NavigationOnClick = {
                                //NavigationOnClick()
//                                scope.launch {
//                                    scaffoldState.drawerState.apply {
//                                        if (isClosed) open() else close()
//                                    }
//                                }
                            },
                            MenuOnClick = {
                                expandedMenu.value = true
                            },
                            AppsOnClick = onNavigateToApps,
                            SearchOnClick = SearchOnClick,
                            SelectOnClick = SelectOnClick
                        )
                    }
                }
                Divider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "目标应用",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WidgetTargetApp(
                            targetAppName = targetAppName,
                            targetAppPackageName = targetAppPackageName,
                            targetAppDescription = targetAppDescription,
                            targetAppVersionName = targetAppVersionName,
                            targetAppChecked = {
                                expandedPowerButton.value = true
                            },
                            targetAppUnchecked = {
                                expandedPowerButton.value = false
                            }
                        )
                    }
                }
                Divider()
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "电源",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            )
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(end = 2.5.dp),
                            enabled = expandedPowerButton.value
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ToggleOn,
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(
                                    text = "ON",
                                    color = Color.Green
                                )
                            }
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 2.5.dp),
                            enabled = expandedPowerButton.value
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ToggleOff,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(
                                    text = "OFF",
                                    color = Color.Red
                                )
                            }

                        }
                    }
                }
                Divider()
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "设备信息",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 2.5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Android,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "sb",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 5.dp,
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 2.5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhoneAndroid,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "sb",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 5.dp,
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 2.5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DesignServices,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "sb",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 5.dp,
                                )
                        )
                    }
                }
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        shape = MaterialTheme.shapes.small.copy(
//                            CornerSize(percent = 10)
//                        ),
//                        backgroundColor = Color(0x99FFFFFF),
//                        elevation = 0.dp,
//                    ) {
//
//                    }

//                    AndroidViewBinding(
//                        factory = FactoryMainBinding::inflate,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                    ) {
//
//                    }


            }
        }
    }
}

@Preview
@Composable
fun ScreenHomePreview() {
    AndrontainerTheme {
        ScreenHome(
            title = "Androntainer",
            targetAppName = "Androntainer",
            targetAppPackageName = "Androntainer",
            targetAppDescription = "Androntainer",
            targetAppVersionName = "1.0",
            NavigationOnClick = {},
            MenuOnClick = {},
            SearchOnClick = {},
            SheetOnClick = {},
            onNavigateToApps = {},
            AppsOnClick = {},
            SelectOnClick = {}
        )
    }
}

@Composable
fun ScreenApps() {

}

@Preview
@Composable
fun ScreenAppsPreview() {
    ScreenApps()
}