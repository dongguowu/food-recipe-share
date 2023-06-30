package com.dishdiscoverers.foodrecipe.xiaowei


import Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Screen class representing the user registration screen.
 */
class RegisterScreen : Screen {
    /**
     * Composable function that displays the content of the registration screen.
     * It includes input fields for username, email, password, and confirm password,
     * along with buttons for submitting the registration, canceling, and navigating to the administrator screen.
     */

    @Composable
    override fun Content() {
        val screenModel =
            rememberScreenModel() { RegistrationScreenModel(AuthRepository()) }

        val authResource = screenModel?.signupFlow?.collectAsState()
        var errorMessage by remember { mutableStateOf("") }


        val state by screenModel.state.collectAsState()
        var userName by remember { mutableStateOf("dongguo") }
        var email by remember { mutableStateOf("dongguo5@wu.com") }
        var password by remember { mutableStateOf("dongguo") }
        var confirmPassword by remember { mutableStateOf("dongguo") }
        val navigator = LocalNavigator.currentOrThrow
        val siteKey =
            "6LeNbMgmAAAAADfFl8Oq8FB-FerIsp32EHiLijy2" // Replace with your reCAPTCHA site key
        val coroutineScope = CoroutineScope(Dispatchers.Main)


        suspend fun runValidation(): Boolean {
            val userName = userName
            val email = email
            val password = password
            val confirmPassword = confirmPassword

            return screenModel.isUsernameUnique(userName) &&
                    screenModel.isEmailValid(email) &&
                    screenModel.isPasswordValid(password) &&
                    screenModel.doPasswordsMatch(password, confirmPassword)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                url = "https://i.pinimg.com/564x/9d/36/fd/9d36fd94e51bdb73759070905718e669.jpg",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                when (val result = state) {
                    is RegistrationScreenModel.State.Init -> Text("...")
                    is RegistrationScreenModel.State.Loading -> Text("Loading")
                    is RegistrationScreenModel.State.Result -> {
                        Text("Success")
                    }
                }
                Text(
                    "Sign Up",
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(top = 35.dp, bottom = 10.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    label = { Text("Enter username") },
                    leadingIcon = {
                        Icon(Icons.Filled.Face, contentDescription = "username icon")
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onSurface,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        backgroundColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    label = { Text("Enter email address") },
                    leadingIcon = {
                        Icon(Icons.Filled.Email, contentDescription = "email icon")
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onSurface,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    label = { Text("Enter password") },
                    leadingIcon = {
                        Icon(Icons.Filled.Lock, contentDescription = "password icon")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onSurface,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    label = { Text("Enter confirm password") },
                    leadingIcon = {
                        Icon(Icons.Filled.Lock, contentDescription = "password icon")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onSurface,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))
                // Error message
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colors.error
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
//                    val isEnabled = runBlocking { runValidation() }
                    val isEnabled = true
                    Button(
                        onClick = {
                            if (screenModel.isEmailValid(email)) {
                                val result = runBlocking {
                                    screenModel.signupUser(email, password)
                                }
                            } else {
                                errorMessage = "Invalidate Email"
                            }

                            authResource?.value?.let {
                                when (it) {
                                    is Resource.Failure -> {
                                        errorMessage = it.exception.message.toString()
                                        println(errorMessage)
                                    }

                                    is Resource.Loading -> {
                                        errorMessage = "validating..."
                                    }

                                    is Resource.Success -> {
                                        navigator.push(ScreenRouter(AllScreens.Login))
                                    }
                                }
                            }


                            // Run the click logic inside a coroutine

//                            screenModel.signupUser("test", "dongguo@wu.com.3", password)
//                            coroutineScope.launch {
////                                if (runValidation()) {
//                                    if (true) {
//                                    screenModel.addUser(
//                                        userName,
//                                        email,
//                                        password,
//                                        confirmPassword
//                                    )
//                                    navigator.push(ScreenRouter(AllScreens.Login))
//                                }
//                            }
                        },
                        modifier = Modifier.padding(10.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                        enabled = isEnabled
                    ) {

                        Text(
                            "Submit",
                            style = MaterialTheme.typography.button,
                            modifier = Modifier.padding(
                                top = 8.dp,
                                start = 30.dp,
                                end = 30.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                }

            }

        }

    }

}
