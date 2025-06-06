package com.example.ZingBite.ui.features.auth.signup

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ZingBite.R
import com.example.ZingBite.ui.ZingBiteTextField
import com.example.ZingBite.ui.features.auth.AuthScreen
import com.example.ZingBite.ui.navigation.AuthScreen
import com.example.ZingBite.ui.navigation.Home
import com.example.ZingBite.ui.navigation.Login
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        val name = viewModel.name.collectAsStateWithLifecycle()
        val email = viewModel.email.collectAsStateWithLifecycle()
        val password = viewModel.password.collectAsStateWithLifecycle()
        val uiState = viewModel.uiState.collectAsState()
        val errorMessage = remember { mutableStateOf<String?>(null) }
        val loading = remember { mutableStateOf(false) }

        when(uiState.value) {
            is SignUpViewModel.SignupEvent.Error -> {
                loading.value = false
                errorMessage.value = "Failed"
            }
            is SignUpViewModel.SignupEvent.Loading -> {
                loading.value = true
                errorMessage.value = null
            }
            else -> {
                loading.value = false
            }
        }

        val context = LocalContext.current
        LaunchedEffect(true) {
            viewModel.navigationEvent.collectLatest { event ->
                when(event) {
                    is SignUpViewModel.SigupNavigationEvent.NavigateToHome -> {
                        navController.navigate(Home) {
                            popUpTo(AuthScreen) {
                                inclusive = true
                            }
                        }
                    }
                    is SignUpViewModel.SigupNavigationEvent.NavigateToLogin -> {
                        navController.navigate(Login)
                    }
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.signup),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(110.dp))

            Text(
                text = "Sign Up",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            ZingBiteTextField(
                value = name.value,
                onValueChange = { viewModel.onNameChange(it) },
                label = {
                    Text(
                        text = "Name",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            ZingBiteTextField(
                value = email.value,
                onValueChange = { viewModel.onEmailChange(it) },
                label = {
                    Text(
                        text = "Email",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            ZingBiteTextField(
                value = password.value,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = {
                    Text(
                        text = "Password",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage.value ?: "", color = Color.Red)

            Button(
                onClick = viewModel::onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE724C))
            ) {
                Box {
                    AnimatedContent(
                        targetState = loading.value,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) togetherWith
                                    fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
                        }
                    ) { target ->
                        if (target) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Sign Up",
                                color = Color.White,
                                fontSize = 17.sp,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account? ", color = Color.Black, fontSize = 16.sp)
                    TextButton(onClick = {
                        viewModel.onLoginClicked()
                    }) {
                        Text(
                            text = "Sign in",
                            color = Color(0xFFFE724C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f).height(1.dp), color = Color.Black)
                Text(
                    text = " ${stringResource(R.string.signin)} ",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f).height(1.dp), color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { viewModel.onGoogleSignInClicked(context) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", color = Color.Black, fontSize = 17.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviousSignUpScreen(){
    SignUpScreen(rememberNavController())
}