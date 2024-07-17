//// BiometricHelper.kt
//
//package com.example.smarttaskmanager.ui.activities
//
//import android.content.Context
//import androidx.biometric.BiometricManager
//import androidx.biometric.BiometricPrompt
//import androidx.core.content.ContextCompat
//
//class BiometricHelper(private val context: Context) {
//
//    private val biometricManager = BiometricManager.from(context)
//
//    fun isBiometricSupported(): Boolean {
//        return biometricManager.canAuthenticate(
//            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
//        ) == BiometricManager.BIOMETRIC_SUCCESS
//    }
//
//    fun authenticate(callback: BiometricPrompt.AuthenticationCallback) {
//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric Authentication")
//            .setSubtitle("Log in using your facial or fingerprint credential")
//            .setAllowedAuthenticators(
//                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
//            )
//            .build()
//
//        val biometricPrompt = BiometricPrompt(
//            HomeActivity(),
//            ContextCompat.getMainExecutor(context),
//            callback
//        )
//        biometricPrompt.authenticate(promptInfo)
//    }
//}
