package com.example.ZingBite.ui.features.auth

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ZingBite.data.FoodApi
import com.example.ZingBite.data.auth.GoogleAuthUiProvider
import com.example.ZingBite.data.models.AuthResponse
import com.example.ZingBite.data.models.OAuthRequest
import com.example.ZingBite.data.remote.ApiResponse
import com.example.ZingBite.data.remote.safeApiCall
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel(open val foodApi: FoodApi) : ViewModel() {
    private val googleAuthUiProvider = GoogleAuthUiProvider()
    var error:String = ""
    var errorDescription:String = ""
    abstract fun loading()
    abstract fun onGoogleError(msg: String)
    abstract fun onSocialLoginSuccess(token: String)
    fun onGoogleClicked(context: ComponentActivity){
        initiateGoogleLogin(context)
    }

    protected fun initiateGoogleLogin(context: ComponentActivity){
        viewModelScope.launch {
            loading()
            try {
                val credentialManager = CredentialManager.create(context)
                try {
                    val response = googleAuthUiProvider.signIn(
                        context,
                        credentialManager
                    )
                    fetchFoodAppToken(response.token, "google"){
                        onGoogleError(it)
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
    private fun fetchFoodAppToken(token: String, provider: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            val request = OAuthRequest(
                token = token, provider = provider
            )
            val res = safeApiCall { foodApi.oAuth(request) }
            when (res) {
                is ApiResponse.Success -> {
                    onSocialLoginSuccess(res.data.token)
                }

                else -> {
                    val error = (res as? ApiResponse.Error)?.code
                    if (error != null) {
                        when (error) {
                            401 -> onError("Invalid Token")
                            500 -> onError("Server Error")
                            404 -> onError("Not Found")
                            else -> onError("Failed")
                        }
                    } else {
                        onError("Failed")
                    }
                }
            }
        }
    }
}