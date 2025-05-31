package com.example.ZingBite.ui.features.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foode.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AuthScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Curved Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(
                    RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.bgfood), // Your banner image
                contentDescription = "Top Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            TextButton(
                onClick = { /* handle skip */ },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFFE724C)
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(48)
            ) {
                Text(
                    text = stringResource(id = R.string.skip),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .padding(start = 16.dp) // consistent left padding for both texts
                .fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Welcome to\n")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFFFE724C))) {
                        append("ZingBite")
                    }
                },
                fontWeight = FontWeight.Bold,
                fontSize = 52.sp,
                lineHeight = 56.sp, // increased line height for better spacing
            )

            Spacer(modifier = Modifier.height(4.dp)) // small vertical space between title and slogan

            Text(
                text = "Hungry?Let ZingBite handle it.",
                fontSize = 20.sp,
                color = Color.Gray,
            )
        }

        // ⬇️ Additional gap before "sign in with"
        Spacer(modifier = Modifier.height(110.dp))

        // ⬇️ Divider with "sign in with"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.LightGray
            )
            Text(
                text = "  sign in with  ",
                color = Color.Gray,
                fontSize = 17.sp
            )
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-In Button
        OutlinedButton(
            onClick = { /* handle google sign in */ },
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

        // Start with Email or Phone Button (Orange)
        Button(
            onClick = { /* handle email/phone */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFE724C)
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Start with Email or Phone", color = Color.White, fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign In Row
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? ", color = Color.Gray, fontSize = 17.sp)
            Text(
                text = "Sign in",
                color = Color(0xFFFE724C),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.clickable { /* handle sign in */ }
            )
        }
    }
}
