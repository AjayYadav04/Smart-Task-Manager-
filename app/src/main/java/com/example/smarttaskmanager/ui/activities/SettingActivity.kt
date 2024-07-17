package com.example.smarttaskmanager.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.smarttaskmanager.R
import com.google.android.datatransport.BuildConfig

class SettingActivity : BaseActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        contentFrameLayout.addView(layoutInflater.inflate(R.layout.activity_setting, null))
        setCustomHeader("Settings", tittleVisible = true, backButtonVisible = true)
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        val nightModeSwitch: Switch = findViewById(R.id.switch_night_mode)
        val notificationsSwitch: Switch = findViewById(R.id.switch_notifications)
        val versionTextView: TextView = findViewById(R.id.version_text_view)

        // Set the switch state based on the current night mode
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        nightModeSwitch.isChecked = nightMode == AppCompatDelegate.MODE_NIGHT_YES

        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("night_mode_enabled", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("night_mode_enabled", false).apply()
            }
            recreate()
        }

        notificationsSwitch.isChecked = sharedPreferences.getBoolean("notifications_enabled", false)

        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableNotifications()
                sharedPreferences.edit().putBoolean("notifications_enabled", true).apply()
            } else {
                disableNotifications()
                sharedPreferences.edit().putBoolean("notifications_enabled", false).apply()
            }
        }

        val versionName = BuildConfig.VERSION_NAME
        versionTextView.text = "Version $versionName"
    }

    private fun enableNotifications() {
        val intent = Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, packageName)
        }
        startActivity(intent)
    }

    private fun disableNotifications() {
        val intent = Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, packageName)
        }
        startActivity(intent)
    }
}
