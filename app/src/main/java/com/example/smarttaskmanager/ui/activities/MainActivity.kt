package com.example.smarttaskmanager.ui.activities

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.utils.AppUtils.setContext

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("night_mode_enabled", false)
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.activity_main)
        setContext(this.applicationContext)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            handleBiometricAuthentication()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleBiometricAuthentication()
        } else {
            showMessage("Camera permission denied. Cannot use facial recognition.")
        }
    }

    private fun handleBiometricAuthentication() {
        if (isBiometricSupported()) {
            showBiometricPrompt()
        } else {
            showMessage("No biometric features available on this device.")
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your facial or fingerprint credential")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showMessage("Authentication error: $errString")
                    finish()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showMessage("Authentication succeeded!")
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showMessage("Authentication failed.")
                    finish()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
    }
}
