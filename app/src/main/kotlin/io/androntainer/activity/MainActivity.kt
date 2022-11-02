package io.androntainer.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.widget.FrameLayout
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
//import com.google.android.material.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.navigation.NavigationView
import io.androntainer.R
import io.androntainer.application.app.MainActivityContentView
import io.androntainer.databinding.ActivityMainBinding
import io.androntainer.databinding.DialogLicenseBinding
import io.androntainer.databinding.SheetMainBinding
import io.androntainer.service.BillingService
import io.androntainer.ui.values.AndrontainerTheme
import kotlin.math.hypot


/**
 * Androntainer Project
 * Copyright (c) 2022 wyq0918dev.
 */
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
        val intent = Intent(context, BillingService::class.java)
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
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
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
//        val view: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
//        val behavior = BottomSheetBehavior.from(view)
//        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
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

