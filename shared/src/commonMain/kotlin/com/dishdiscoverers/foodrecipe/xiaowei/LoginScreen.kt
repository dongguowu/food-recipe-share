package com.dishdiscoverers.foodrecipe.xiaowei

import Image
import androidx.compose.foundation.Image
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dishdiscoverers.foodrecipe.dongguo.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryTheMealAPI
import com.dishdiscoverers.foodrecipe.dongguo.RecipeScreenModel
import com.dishdiscoverers.foodrecipe.dongguo.Resource
import com.dishdiscoverers.foodrecipe.dongguo.UserRecipeCommentRepositoryFirebase
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi


/**
 * Represents the login screen.
 * 646240160192-tm0s4r2rnt9q6irmo3a05pgvgannfh05.apps.googleusercontent.com
 * 646240160192-6fluksqseop07doqln856tog0d9ist7e.apps.googleusercontent.com
 * https://github.com/stevdza-san/OneTapCompose
 * web client ID:534012659532-1c8bd01ni8klahd55pgem45ks1r7vdtu.apps.googleusercontent.com
 * Web client secret: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
 */
class LoginScreen : Screen {

    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                apiRepository = RecipeRepositoryTheMealAPI(),
                authRepository = AuthRepository(),
                commentRepository = UserRecipeCommentRepositoryFirebase(authRepository = AuthRepository()),
            )
        }

        var currentUser = screenModel.currentUser
        val state by screenModel.state.collectAsState()
        val authResource = screenModel?.loginFlow?.collectAsState()
        //Todo: delete dongguo password
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isClicked by remember { mutableStateOf(true) }
        val navigator = LocalNavigator.currentOrThrow
        var errorMessage by remember { mutableStateOf("") }



        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                url = "https://i.pinimg.com/564x/9d/36/fd/9d36fd94e51bdb73759070905718e669.jpg",
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()

            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center)
            ) {


                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = 8.dp,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    backgroundColor = Color.White,
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        content = {

                            //TODO
//                            when (val result = state) {
//                                is LoginScreenModel.State.Init -> Text("...")
//                                is LoginScreenModel.State.Loading -> Text("Loading")
//                                is LoginScreenModel.State.Result -> {
//                                    Text("Success")
//                                }
//
//                                else -> {
//                                    Text("Invalid email or password", color = Color.Red)
//                                }
//                            }
//                                ShinyText(
//                                    text = "LoginMe",
//                                    modifier = Modifier.padding(vertical = 50.dp)
//                                        .align(Alignment.CenterHorizontally)
//                                )
                            Text(
                                "LoginMe",
                                color = MaterialTheme.colors.onSecondary,
                                fontSize = 30.sp,
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 50.dp),
                                textAlign = TextAlign.Center
                            )


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
                                    Icon(
                                        Icons.Filled.Lock,
                                        contentDescription = "password icon"
                                    )
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = MaterialTheme.colors.onSurface,
                                    focusedBorderColor = MaterialTheme.colors.primary,
                                    unfocusedBorderColor = MaterialTheme.colors.onSurface
                                )
                            )
                            // Error message
                            if (errorMessage.isNotEmpty()) {
                                Text(
                                    text = errorMessage,
                                    color = MaterialTheme.colors.error
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                "New user? Click here",
                                modifier = Modifier.padding(10.dp)
                                    .clickable(onClick = {
                                        navigator.push(
                                            ScreenRouter(
                                                AllScreens.Register
                                            )
                                        )
                                    }),
                                color = MaterialTheme.colors.onBackground,
                                textDecoration = TextDecoration.Underline,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Button(
                                onClick = {
                                    val result = runBlocking {
                                        screenModel.loginUser(email, password)
                                    }
                                    //TODO
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
                                                navigator.push(
                                                    ScreenRouter(
                                                        AllScreens.Profile(
                                                            email
                                                        )
                                                    )
                                                )
                                            }
                                        }
                                    }
//                                    when (result) {
//                                        is LoginScreenModel.LoginResult.Success -> {
//
//                                            navigator.push(ScreenRouter(AllScreens.Profile(email)))
//
//                                        }
//
//                                        is LoginScreenModel.LoginResult.Error -> {
//                                            // Show error message
//                                            errorMessage = "Invalid credentials"
//                                        }
//                                    }

                                }, modifier = Modifier.padding(10.dp),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = MaterialTheme.colors.onBackground
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),

                                enabled = !email.isEmpty() && !password.isEmpty()
                            ) {

                                Text(
                                    "Login",
                                    style = MaterialTheme.typography.button,
                                    modifier = Modifier.padding(
                                        top = 8.dp,
                                        start = 30.dp,
                                        end = 30.dp,
                                        bottom = 8.dp
                                    )
                                )
                            }


                            Text("or sign in with")
                            Spacer(modifier = Modifier.height(15.dp))
                            Image(
                                url = "https://cdn-icons-png.flaticon.com/512/281/281764.png", // Replace with the URL of the Google logo image
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                                    .clickable(onClick = {})
                            )

                        }
                    )
                }
            }
        }

    }
//    private fun signInWithGoogle() {
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(LocalContext.current, googleSignInOptions)
//        val signInIntent = googleSignInClient.signInIntent
//        LocalContext.current.startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

}


/**
 * Changes the color based on whether the button is clicked or not.
 *
 * @param isClicked Determines whether the button is clicked or not.
 * @return The color to be used for the text.
 */
@Composable
fun colorChangeByClick(isClicked: Boolean): Color {

    return if (isClicked) {
        MaterialTheme.colors.onSecondary
    } else {
        MaterialTheme.colors.error
    }
}

@Composable
fun ShinyText(text: String, modifier: Modifier = Modifier) {
    var isShowing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            isShowing = !isShowing
        }
    }

    val shimmerColor = animateColorAsState(
        targetValue = if (isShowing) MaterialTheme.colors.onSecondary else Color.Transparent,
        animationSpec = tween(durationMillis = 500)
    )

    Text(
        text = text,
        color = shimmerColor.value,
        fontSize = 30.sp,
        style = MaterialTheme.typography.h3,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
