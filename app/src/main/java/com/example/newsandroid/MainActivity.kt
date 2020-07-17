package com.example.newsandroid

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.newsandroid.MyApp.Companion.globalLanguage
import com.example.newsandroid.enums.Language
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    companion object {
        var dLocale: Locale? = null
    }

    init {
        updateConfig(this)
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale==Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }

    private lateinit var sharedPreferences: SharedPreferences
    fun getFragmentRefreshListener(): FragmentRefreshListener? {
        return fragmentRefreshListener
    }

    fun setFragmentRefreshListener(fragmentRefreshListener: FragmentRefreshListener?) {
        this.fragmentRefreshListener = fragmentRefreshListener
    }

    private var fragmentRefreshListener: FragmentRefreshListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("com.exemple.newsAndroid", Context.MODE_PRIVATE)
        globalLanguage = if (sharedPreferences.getString("GlobalLanguage","FR")=="FR")
            Language.FR
        else
            Language.US
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailNewsFragment -> {
                    bottom_navigation?.visibility = View.GONE
                }
                else -> bottom_navigation?.visibility = View.VISIBLE
            }
        }


    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.topHeadlinesFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.topHeadlinesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.everythingFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.everythingFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.android_action_bar_button_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val menuItemView: View = findViewById(R.id.flagButton)

        val popupMenu = PopupMenu(this, menuItemView)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.action_fr -> {
                    globalLanguage = Language.FR
                    sharedPreferences.edit().putString("GlobalLanguage", Language.FR.country).apply()
                    dLocale = Locale(Language.FR.language)
                    sharedPreferences.edit().putString(getString(R.string.language_app),Language.FR.language).apply()
                    //LocaleHelper.setLocale(applicationContext, "fr")
                    popupMenu.dismiss()
                    //It is required to recreate the activity to reflect the change in UI.
                    recreate()
                    if(getFragmentRefreshListener()!=null){
                        getFragmentRefreshListener()?.onRefresh();
                    }
                    true
                }
                R.id.action_us -> {
                    globalLanguage = Language.US
                    sharedPreferences.edit().putString("GlobalLanguage", Language.US.country).apply()
                    dLocale = Locale(Language.US.language)
                    sharedPreferences.edit().putString(getString(R.string.language_app),Language.US.language).apply()
                    //LocaleHelper.setLocale(applicationContext, "en")
                    popupMenu.dismiss()
                    //It is required to recreate the activity to reflect the change in UI.
                    recreate()
                    if(getFragmentRefreshListener()!=null){
                        getFragmentRefreshListener()?.onRefresh();
                    }
                    true
                }
                else -> false
            }
        }
        popupMenu.inflate(R.menu.popup_menu)
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        }
        when (item.itemId) {
            R.id.flagButton -> popupMenu.show()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    interface FragmentRefreshListener {
        fun onRefresh()
    }

}