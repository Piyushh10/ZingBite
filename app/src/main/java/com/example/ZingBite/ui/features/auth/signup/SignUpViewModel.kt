package com.example.ZingBite.ui.features.auth.signup

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
import com.example.ZingBite.data.models.SignInRequest
import com.example.ZingBite.data.models.SignUpRequest
import com.example.ZingBite.ui.features.auth.login.SignInViewModel.SigInNavigationEvent
import com.example.ZingBite.ui.features.auth.login.SignInViewModel.SignInEvent
import com.example.ZingBite.data.auth.GoogleAuthUiProvider


@HiltViewModel
class SignUpViewModel @Inject constructor(val foodApi: FoodApi) :
    ViewModel() {
    val googleAuthUiProvider = GoogleAuthUiProvider()
    private val _uiState = MutableStateFlow<SignupEvent>(SignupEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SigupNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.value = SignupEvent.Loading
            try {
                val response = foodApi.signUp(
                    SignUpRequest(
                        email = email.value, password = password.value, name = name.value
                    )
                )
                _uiState.value = SignupEvent.Success
                _navigationEvent.emit(SigupNavigationEvent.NavigateToHome)
            } catch(e: Exception) {
                e.printStackTrace()
                _uiState.value = SignupEvent.Error
            }
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SigupNavigationEvent.NavigateToLogin)
        }
    }

    fun loading() {
        viewModelScope.launch {
            _uiState.value = SignupEvent.Loading
        }
    }

    fun onGoogleSignInClicked(context : Context) {
        viewModelScope.launch {
            _uiState.value = SignupEvent.Loading
            try {
                val response = googleAuthUiProvider.signIn(
                    context,
                    CredentialManager.create(context)
                )
                if(response != null) {
                    _uiState.value = SignupEvent.Success
                    _navigationEvent.emit(SigupNavigationEvent.NavigateToHome)
                } else {
                    _uiState.value = SignupEvent.Error
                }
            } catch(e: Exception) {
                e.printStackTrace()
                _uiState.value = SignupEvent.Error
            }
        }
    }

    sealed class SigupNavigationEvent {
        object NavigateToLogin : SigupNavigationEvent()
        object NavigateToHome : SigupNavigationEvent()
    }

    sealed class SignupEvent {
        object Nothing : SignupEvent()
        object Success : SignupEvent()
        object Error : SignupEvent()
        object Loading : SignupEvent()
    }
}