package com.example.ZingBite.ui.features.auth

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ZingBite.data.FoodApi
import com.example.ZingBite.data.auth.GoogleAuthUiProvider
import com.example.ZingBite.data.models.OAuthRequest
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel(open val foodApi: FoodApi) : ViewModel() {
    private val googleAuthUiProvider = GoogleAuthUiProvider()
    abstract fun loading()
    abstract fun onGoogleError(msg: String)
    abstract fun onSocialLoginSuccess(token: String)
    fun onGoogleClicked(context: ComponentActivity){
        initiateGoogleLogin(context)
    }
    protected fun initiateGoogleLogin(context: ComponentActivity){
        viewModelScope.launch {
            try {
                Log.d("SignInViewModel", "Starting Google Sign-In flow")
                loading()
                val credentialManager = CredentialManager.create(context)
                Log.d("SignInViewModel", "Created CredentialManager")

                try {
                    val response = googleAuthUiProvider.signIn(
                        context,
                        credentialManager
                    )
                    Log.d("SignInViewModel", "Got response from Google Sign-In: ${response != null}")

                    if(response != null) {
                        Log.d("SignInViewModel", "Google Sign-In successful, token length: ${response.token.length}")
                        val request = OAuthRequest(
                            token = response.token,
                            provider = "google"
                        )
                        Log.d("SignInViewModel", "Making OAuth request to backend")
                        try {
                            val res = foodApi.oAuth(request)
                            Log.d("SignInViewModel", "Got response from OAuth endpoint: ${res != null}")
                            if(res.token.isNotEmpty()){
                                onSocialLoginSuccess(res.token)
                            }
                            else {
                                onGoogleError("Failed")
                            }
                        } catch (e: Exception) {
                            onGoogleError("Failed")
                        }
                    }
                    else {
                        onGoogleError("Failed")
                    }
                } catch (e: GetCredentialException) {
                    Log.e("SignInViewModel", "Error during credential retrieval: ${e.message}", e)
                    if (e.message?.contains("canceled", ignoreCase = true) == true) {
                        onGoogleError("Failed")
                    } else {
                        onGoogleError("Failed")
                    }
                }
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during Google Sign-In", e)
                e.printStackTrace()
                onGoogleError("Failed")
            }
        }
    }
}