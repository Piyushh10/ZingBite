package com.example.ZingBite.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ZingBite.R

@Composable
fun SignInDivider(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.signin),
    textColor: Color = Color.Gray,
    dividerColor: Color = Color.LightGray,
    textSize: Int = 16
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = dividerColor
        )
        Text(
            text = " $text ",
            color = textColor,
            fontSize = textSize.sp
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = dividerColor
        )
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Continue with Google",
    iconSize: Int = 24
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "Google Icon",
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.Black, fontSize = 17.sp)
    }
}

@Composable
fun EmailSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Start with Email or Phone",
    backgroundColor: Color = Color(0xFFFE724C),
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = textColor, fontSize = 17.sp)
    }
}

@Composable
fun SignInButtons(
    onGoogleClick: () -> Unit,
    onEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
    googleButtonText: String = "Continue with Google",
    emailButtonText: String = "Start with Email or Phone"
) {
    Column(modifier = modifier) {
        SignInDivider()
        Spacer(modifier = Modifier.height(16.dp))
        GoogleSignInButton(
            onClick = onGoogleClick,
            text = googleButtonText
        )
        Spacer(modifier = Modifier.height(16.dp))
        EmailSignInButton(
            onClick = onEmailClick,
            text = emailButtonText
        )
    }
}


@Composable
fun ZingBiteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.4f),
    )
) {
    Column(Modifier.padding(vertical = 8.dp)) {
        label?.let {
            Row {
                Spacer(modifier = Modifier.size(4.dp))
                it()
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange,
            modifier,
            enabled,
            readOnly,
            textStyle.copy(fontWeight = FontWeight.SemiBold),
            null,
            placeholder,
            leadingIcon,
            trailingIcon,
            prefix,
            suffix,
            supportingText,
            isError,
            visualTransformation,
            keyboardOptions,
            keyboardActions,
            singleLine,
            maxLines,
            minLines,
            interactionSource,
            shape,
            colors
        )
    }
}
