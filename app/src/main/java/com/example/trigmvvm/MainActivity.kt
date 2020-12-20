package com.example.trigmvvm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.trigmvvm.ui.main.MainFragment
import com.example.trigmvvm.ui.main.SplashFragment

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
const val PREFS_NAME = "theme_prefs"
const val KEY_THEME = "prefs.theme"
const val THEME_UNDEFINED = -1

class MainActivity : AppCompatActivity() {
    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu?.findItem(R.id.night_mode)?.setTitle(R.string.day_mode)
        } else {
            menu?.findItem(R.id.night_mode)?.setTitle(R.string.night_mode)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.night_mode) {
            val nightMode = AppCompatDelegate.getDefaultNightMode()
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveTheme(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            // Recreate the activity for the theme change to take effect.
            recreate()
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.isNotEmpty()) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        //TODO find a better way of handling not having the appropriate permissions
        if (!permissionToRecordAccepted) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SplashFragment.newInstance())
                    .commitNow()
        }
    }

    fun moveToNext() {
        Log.d("Main Acitivity","Splash screen stopped")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }

    private fun setAppTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(themeMode)
    }

    fun initTheme() {
        setAppTheme(getSavedTheme())
    }

    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(KEY_THEME, theme).apply()

    private fun getSavedTheme() = sharedPrefs.getInt(KEY_THEME, THEME_UNDEFINED)
}