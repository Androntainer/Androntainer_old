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
import android.view.ViewGroup.LayoutParams
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
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
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Launch
import androidx.compose.material.icons.twotone.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.*
import com.android.vending.billing.IInAppBillingService
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.color.DynamicColors
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


/**
 ***************************************************************************************************
 * Abstract 抽象类
 ***************************************************************************************************
 */

abstract class AndrontainerApplication : BaseApp<Androntainer>() {

    private val context: BaseApp<Androntainer> = me

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

    protected val app: BaseApp<Androntainer> = context
    abstract fun initSdk()
}

abstract class AndrontainerActivity : BaseActivity(), Runnable {

    // 获取上下文
    private val context: BaseActivity = me

    // 布局
    override fun resetContentView(): View? {
        initView()
        setSupportActionBar(actionBar())
        return contentView()
    }

    override fun initViews() {
        initServices()
        initSystemBar()
        initUi()
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

    // 服务启动相关
    protected val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {

        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }

    // 供子类使用的上下文
    protected val viewContext: BaseActivity = context
    protected val thisContext: BaseActivity = context

    // 需要子类重写的方法
    abstract fun initView()
    abstract fun contentView(): View?
    abstract fun actionBar(): Toolbar?
    abstract fun runnable()
    abstract fun postAnim()
    abstract fun initServices()
    abstract fun initData(parameter: JumpParameter?)
    abstract fun initSystemBar()
    abstract fun initUi()
    abstract fun setEvent()
}

abstract class LibraryActivity : BaseActivity() {

    private val context: BaseActivity = me

    override fun resetContentView(): View {
        return View(me)
    }

    override fun initViews() {
        create()
    }

    override fun initDatas(parameter: JumpParameter?) {
        data(parameter)
    }

    override fun setEvents() {
    }

    protected val thisContext: BaseActivity = context

    abstract fun create()
    abstract fun data(parameter: JumpParameter?)
}

abstract class FixedActivity : BaseActivity() {

    private val context: BaseActivity = me

    override fun resetContentView(): View? {
        initView()
        setSupportActionBar(actionBar())
        return contentView()
    }

    override fun initViews() {

    }

    override fun initDatas(parameter: JumpParameter?) {

    }

    override fun setEvents() {

    }

    protected val thisContext: BaseActivity = context

    abstract fun initView()
    abstract fun contentView(): View?
    abstract fun actionBar(): Toolbar?

}

abstract class AndrontainerFragment<me : AndrontainerActivity> : BaseFragment<me>() {

    override fun initViews() {

    }

    override fun initDatas() {

    }

    override fun setEvents() {

    }
}

/**
 ***************************************************************************************************
 * Activity 用户界面
 ***************************************************************************************************
 */

class Androntainer : AndrontainerApplication() {

    override fun initSdk() {
        initDynamicColors(app)
        initDialogX(app)
        initTaskbar(app)
    }
}

class MainActivity : AndrontainerActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var logo: AppCompatImageView
    private lateinit var greeting: ComposeView
    private lateinit var layout: LinearLayoutCompat

    private var targetAppName: String by mutableStateOf("unknown")
    private var targetPackageName: String by mutableStateOf("unknown")
    private var targetDescription: String by mutableStateOf("unknown")

    private var androidVersion: String = "unknown"

    override fun initView() {
        toolbar = MaterialToolbar(
            viewContext
        ).apply {
            popupTheme = R.style.ThemeOverlay_Androntainer_NoActionBar_PopupOverlay
            navigationIcon = ContextCompat.getDrawable(
                viewContext,
                R.drawable.ic_baseline_menu_24
            )
            subtitle = targetAppName
        }

        logo = AppCompatImageView(
            viewContext
        ).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    viewContext,
                    R.drawable.ic_baseline_androntainer_plat_logo_24
                )
            )
        }

        greeting = ComposeView(
            viewContext
        ).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }

        layout = LinearLayoutCompat(
            viewContext
        ).apply {
            orientation = LinearLayoutCompat.VERTICAL
            fitsSystemWindows = true
            addView(
                AppBarLayout(
                    viewContext
                ).apply {
                    setTheme(com.google.android.material.R.style.ThemeOverlay_Material3_ActionBar)
                    addView(
                        toolbar,
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                        )
                    )
                },
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                CoordinatorLayout(
                    viewContext
                ).apply {
                    addView(
                        greeting,
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT
                        )
                    )
                    addView(
                        logo,
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT
                        )
                    )
                },
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun contentView(): View {
        return layout
    }

    override fun actionBar(): Toolbar {
        return toolbar
    }

    /**
     * Splash animation
     */

    override fun runnable() {
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
        greeting.visibility = View.INVISIBLE
        greeting.postDelayed(this, 200)
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


    }

    override fun setEvent() {
        setGlobalLifeCircleListener(
            object : GlobalLifeCircleListener() {
                override fun onCreate(me: BaseActivity, className: String) {
                    super.onCreate(me, className)
                    when (me) {
                        FixedPlay() -> finish()
                    }
                }

                override fun onResume(me: BaseActivity, className: String) {
                    super.onResume(me, className)

                }

                override fun onDestroy(me: BaseActivity, className: String) {
                    super.onDestroy(me, className)
                    when (me) {
                        MainActivity() -> greeting.removeCallbacks(this@MainActivity)
                    }
                }

                override fun onStart(activity: BaseActivity?, className: String?) {
                    super.onStart(activity, className)
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
     * Jetpack Compose Layout
     */

    @Composable
    private fun Layout() {
        MainActivityContentView(
            title = "title",
            targetAppName = targetAppName,
            NavigationOnClick = {

            },
            MenuOnClick = {
                optionsMenu()
            },
            SearchOnClick = {

            },
            SheetOnClick = {

            },
            AppsOnClick = {

            },
            SelectOnClick = {
                select()
            }
        )
    }

    /**
     * Jetpack Compose layout preview
     */

    @Preview(showBackground = true)
    @Composable
    fun ActivityMainPreview() {
        AndrontainerTheme {
            Layout()
        }
    }

    private fun optionsMenu() {
        toolbar.showOverflowMenu()
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

class FixedPlay : LibraryActivity() {

    private lateinit var mPackageManager: PackageManager
    private val thisPackage = AppUtils.getAppPackageName()
    private var mode: String? = "r2"

    override fun create() {
        mPackageManager = packageManager
    }

    override fun data(parameter: JumpParameter?) {
        go()
    }

    private fun go() {
        val read = PreferenceManager.getDefaultSharedPreferences(thisContext)
        val app = read.getString("app", "")
        val className = read.getString("class", "")
        val perMode: String? = read.getString("mode", "r2")
        if (perMode != "r2") {
            mode = perMode
        }
        if (app!!.isNotEmpty() && app != thisPackage) {
            when (mode) {
                "r2" -> {
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
                    startActivity(intent)
                }
            }
        } else {
            val intent = Intent(thisContext, FixedSettings().javaClass)
            startActivity(intent)
        }
    }
}

class FixedSettings : AppCompatActivity() {

    private lateinit var mPackageManager: PackageManager
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
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private var _mode: String? = null
    private var _uri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityApplistBinding.inflate(layoutInflater)

        val layout = LinearLayoutCompat(this@SelectOne).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                AppBarLayout(this@SelectOne).apply {
                    setTheme(com.google.android.material.R.style.ThemeOverlay_Material3_ActionBar)
                    addView(
                        MaterialToolbar(this@SelectOne).apply {

                        },
                        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    )
                },
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                ProgressBar(this@SelectOne).apply {

                },
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                ListView(this@SelectOne).apply {

                },
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
            )
        }

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

/**
 ***************************************************************************************************
 * Fragment 用户界面
 ***************************************************************************************************
 */

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
            launcherSettings(requireActivity())
            true
        }
        default?.setOnPreferenceClickListener {
            defaultLauncher(requireActivity())
            true
        }
    }
}

/**
 ***************************************************************************************************
 * Services 后台服务
 ***************************************************************************************************
 */

class InAppBillingService : Service() {

    private val tag: String = "FakeInAppStore"

    private lateinit var mInAppBillingService: IInAppBillingService.Stub

    private fun stub() {
        mInAppBillingService =
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
                    return getPurchasesV6(
                        apiVersion,
                        packageName,
                        type,
                        continuationToken,
                        Bundle()
                    )
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
                override fun onTransact(
                    code: Int,
                    data: Parcel,
                    reply: Parcel?,
                    flags: Int
                ): Boolean {
                    if (super.onTransact(code, data, reply, flags)) return true
                    Log.d(
                        tag,
                        "onTransact [unknown]: $code, $data, $flags"
                    )
                    return false
                }
            }
    }


    override fun onBind(intent: Intent?): IBinder {
        stub()
        return mInAppBillingService
    }
}

/**
 ***************************************************************************************************
 * Class 类
 ***************************************************************************************************
 */

/**
 * 导航栏配置
 */

sealed class Screen(
    val route: String,
    val imageVector: ImageVector,
    @StringRes val resourceId: Int
) {
    object Navigation : Screen(routeNavigation, Icons.TwoTone.Navigation, R.string.menu_navigation)
    object Launcher : Screen(routeLauncher, Icons.TwoTone.Launch, R.string.menu_launcher)
    object Home : Screen(routeHome, Icons.TwoTone.Home, R.string.menu_home)
    object Dashboard : Screen(routeDashboard, Icons.TwoTone.Dashboard, R.string.menu_dashboard)
}

/**
 * 选项配置
 */

class Item(
    val name: String,
    val packageName: String,
    val className: String,
    val appIcon: Drawable
)

/**
 * 选项适配器
 */

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

/**
 * 加载动态颜色
 */

fun initDynamicColors(application: Application) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (sharedPreferences.getBoolean("dynamic_colors", true)) {
            DynamicColors.applyToActivitiesIfAvailable(application)
        }
    }
}

/**
 * 初始化DialogX
 */

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

/**
 * 初始化任务栏
 */

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

/**
 * 布局代码入口
 */

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

fun launcherSettings(context: Context) {
    val intent = Intent(context, FixedSettings().javaClass)
    context.startActivity(intent)
}

/**
 * 打开桌面
 */

fun startLauncher(context: Context) {
    val intent = Intent(context, FixedPlay().javaClass)
    context.startActivity(intent)
}

/**
 ***************************************************************************************************
 * 资源
 ***************************************************************************************************
 */

// 目标应用包名配置
const val aidlux: String = "com.aidlux"
const val termux: String = ""

// 导航路由配置
const val routeNavigation: String = "navigation"
const val routeLauncher: String = "launcher"
const val routeHome: String = "home"
const val routeDashboard: String = "dashboard"

// 颜色
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// 样式
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

// 深色主题
val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// 浅色主题
val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// 应用主题配置
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
            (view.context as Activity).window.navigationBarColor = colorScheme.primary.toArgb()
            @Suppress("DEPRECATION")
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

@Preview(showBackground = true)
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

@Preview(showBackground = true)
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

@Preview(showBackground = true)
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val items = listOf(
        Screen.Navigation,
        Screen.Launcher,
        Screen.Home,
        Screen.Dashboard
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.imageVector,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    id = screen.resourceId
                                )
                            )
                        },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(
                                route = screen.route
                            ) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                   // navController.navigate("apps")
//                },
//                modifier = Modifier.navigationBarsPadding()
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowForward,
//                    contentDescription = null
//                )
//            }
//        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Navigation.route) {

            }
            composable(Screen.Launcher.route) {

            }
            composable(Screen.Home.route) {
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
                    onNavigateToApps = {

                    },
                )
            }
            composable(Screen.Dashboard.route) {

            }
        }
    }
}

@Preview(showBackground = true)
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
    val expandedMenu = remember {
        mutableStateOf(false)
    }
    val expandedPowerButton = remember {
        mutableStateOf(true)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
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

        }
    }
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
////                Image(
////                    painter = painterResource(id = R.drawable.background),
////                    contentDescription = null,
////                    modifier = Modifier.fillMaxSize(),
////                    contentScale = ContentScale.FillBounds
////                )
//
//    }
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                title = {
//                    Column {
//                        Text(text = title)
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth(),
//                navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            //NavigationOnClick()
//
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = null
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(
//                        onClick = {
//                            expandedMenu.value = true
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.MoreVert,
//                            contentDescription = null
//                        )
//                    }
//                    DropdownMenu(
//                        expanded = expandedMenu.value,
//                        onDismissRequest = {
//                            expandedMenu.value = false
//                        },
//                    ) {
//                        DropdownMenuItem(
//                            text = {
//                                Text("分享")
//                            },
//                            onClick = {
//
//                                expandedMenu.value = false
//                            }
//                        )
//                        Divider()
//                        DropdownMenuItem(
//                            text = {
//                                Text("抽屉")
//                            },
//                            onClick = {
////                                scope.launch {
////                                    scaffoldState.drawerState.apply {
////                                        if (isClosed) open() else close()
////                                    }
////                                }
////                                expandedMenu.value = false
//                            }
//                        )
//                        DropdownMenuItem(
//                            text = {
//                                Text("更多")
//                            },
//                            onClick = {
//                                expandedMenu.value = false
//                                MenuOnClick()
//                            }
//                        )
//                    }
//                },
//            )
//        },
//        bottomBar = {
//
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = onNavigateToApps,
//                modifier = Modifier.navigationBarsPadding()
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowForward,
//                    contentDescription = null
//                )
//            }
//        },
//        floatingActionButtonPosition = FabPosition.End,
//    ) { innerPadding ->
//
//    }
}

@Preview(showBackground = true)
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

@Preview(showBackground = true)
@Composable
fun ScreenAppsPreview() {
    ScreenApps()
}