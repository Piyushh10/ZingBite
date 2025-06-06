package com.example.ZingBite.ui.features.auth.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import com.example.ZingBite.data.FoodApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.ZingBite.data.auth.GoogleAuthUiProvider
import com.example.ZingBite.data.models.SignInRequest
import com.example.ZingBite.data.models.SignUpRequest
import android.util.Log
import androidx.credentials.exceptions.GetCredentialException
import com.example.ZingBite.data.models.OAuthRequest

@HiltViewModel
class SignInViewModel @Inject constructor(
    val foodApi: FoodApi) : ViewModel(){
        val googleAuthUiProvider = GoogleAuthUiProvider()
    private val _uiState = MutableStateFlow<SignInEvent>(SignInEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SigInNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading
            try {
                val response = foodApi.signIn(
                    SignInRequest(
                        email = email.value, password = password.value
                    )
                )
                _uiState.value = SignInEvent.Success
                _navigationEvent.emit(SigInNavigationEvent.NavigateToHome)
            } catch(e: Exception) {
                e.printStackTrace()
                _uiState.value = SignInEvent.Error
            }
        }
    }

    fun onGoogleSignInClicked(context : Context) {
        viewModelScope.launch {
            try {
                Log.d("SignInViewModel", "Starting Google Sign-In flow")
                _uiState.value = SignInEvent.Loading
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
                                Log.d("SignInViewModel", "OAuth successful, token length: ${res.token.length}")
                                _uiState.value = SignInEvent.Success
                                _navigationEvent.emit(SigInNavigationEvent.NavigateToHome)
                            }
                            else {
                                Log.e("SignInViewModel", "Empty token in OAuth response")
                                _uiState.value = SignInEvent.Error
                            }
                        } catch (e: Exception) {
                            Log.e("SignInViewModel", "Error during OAuth request: ${e.message}", e)
                            _uiState.value = SignInEvent.Error
                        }
                    }
                    else {
                        Log.e("SignInViewModel", "Null response from Google Sign-In")
                        _uiState.value = SignInEvent.Error
                    }
                } catch (e: GetCredentialException) {
                    Log.e("SignInViewModel", "Error during credential retrieval: ${e.message}", e)
                    if (e.message?.contains("canceled", ignoreCase = true) == true) {
                        _uiState.value = SignInEvent.Nothing
                    } else {
                        _uiState.value = SignInEvent.Error
                    }
                }
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during Google Sign-In", e)
                e.printStackTrace()
                _uiState.value = SignInEvent.Error
            }
        }
    }

    fun onSignUpClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SigInNavigationEvent.NavigateToSignUp)
        }
    }

    sealed class SigInNavigationEvent {
        object NavigateToSignUp : SigInNavigationEvent()
        object NavigateToHome : SigInNavigationEvent()
    }

    sealed class SignInEvent {
        object Nothing : SignInEvent()
        object Success : SignInEvent()
        object Error : SignInEvent()
        object Loading : SignInEvent()
    }

    fun loading() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading
        }
    }

    fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Success
            _navigationEvent.emit(SigInNavigationEvent.NavigateToHome)
        }
    }

}