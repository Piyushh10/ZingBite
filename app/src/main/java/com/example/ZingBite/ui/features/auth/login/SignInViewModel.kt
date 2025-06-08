package com.example.ZingBite.ui.features.auth.login

import android.content.Context
import com.example.ZingBite.data.FoodApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.ZingBite.data.models.SignInRequest
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import com.example.ZingBite.data.auth.GoogleAuthUiProvider
import com.example.ZingBite.ui.features.auth.AuthScreenViewModel.AuthEvent
import com.example.ZingBite.ui.features.auth.BaseAuthViewModel
import com.example.ZingBite.ui.features.auth.signup.SignUpViewModel.SignupEvent
import com.example.ZingBite.ui.features.auth.signup.SignUpViewModel.SigupNavigationEvent

@HiltViewModel
class SignInViewModel @Inject constructor(
    override val foodApi: FoodApi) : BaseAuthViewModel(foodApi){
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
            _uiState.value = SignInEvent.Loading
            try {
                val response = googleAuthUiProvider.signIn(
                    context,
                    CredentialManager.create(context)
                )
                if(response != null) {
                    _uiState.value = SignInEvent.Success
                    _navigationEvent.emit(SigInNavigationEvent.NavigateToHome)
                } else {
                    _uiState.value = SignInEvent.Error
                }
            } catch(e: Exception) {
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

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            errorDescription = msg
            error = "Google Sign In Failed"
            _uiState.value= SignInEvent.Error
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Success
            _navigationEvent.emit(SigInNavigationEvent.NavigateToHome)
        }
    }

}