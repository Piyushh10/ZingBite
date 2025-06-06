package com.example.ZingBite.data.auth

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.ZingBite.data.models.GoogleAccount
import com.example.ZingBite.ui.GoogleServerClientID

class GoogleAuthUiProvider {
    suspend fun signIn(
        activityContext: Context,
        credentialManager: CredentialManager
    ): GoogleAccount {
        try {
            Log.d("GoogleAuthUiProvider", "Starting signIn process")
            val request = getCredentialRequest()
            Log.d("GoogleAuthUiProvider", "Created credential request with client ID: $GoogleServerClientID")
            
            val result = try {
                credentialManager.getCredential(
                    activityContext,
                    request
                )
            } catch (e: GetCredentialException) {
                Log.e("GoogleAuthUiProvider", "GetCredentialException: ${e.message}", e)
                throw e
            }
            
            Log.d("GoogleAuthUiProvider", "Got credentials from manager")
            return handleCredentials(result.credential)
        } catch (e: Exception) {
            Log.e("GoogleAuthUiProvider", "Error during signIn", e)
            throw e
        }
    }

    fun handleCredentials(creds: Credential): GoogleAccount {
        try {
            Log.d("GoogleAuthUiProvider", "Handling credentials of type: ${creds.javaClass.simpleName}")
            when {
                creds is CustomCredential && creds.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(creds.data)
                    Log.d("GoogleAuthUiProvider", "Successfully created GoogleIdTokenCredential")
                    
                    if (googleIdTokenCredential.idToken.isBlank()) {
                        Log.e("GoogleAuthUiProvider", "Received empty ID token")
                        throw IllegalStateException("Empty ID token received")
                    }
                    
                    return GoogleAccount(
                        token = googleIdTokenCredential.idToken,
                        displayName = googleIdTokenCredential.displayName ?: "",
                        profileImageUrl = googleIdTokenCredential.profilePictureUri?.toString()
                    )
                }
                else -> {
                    Log.e("GoogleAuthUiProvider", "Invalid credential type: ${creds.javaClass.simpleName}")
                    throw IllegalStateException("Invalid credential type: ${creds.javaClass.simpleName}")
                }
            }
        } catch (e: Exception) {
            Log.e("GoogleAuthUiProvider", "Error handling credentials", e)
            throw e
        }
    }

    private fun getCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(
                GetSignInWithGoogleOption.Builder(
                    GoogleServerClientID
                ).build()
            )
            .build()
    }
}