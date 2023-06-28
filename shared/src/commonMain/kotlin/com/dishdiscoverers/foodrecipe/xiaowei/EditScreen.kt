package com.dishdiscoverers.foodrecipe.xiaowei

import Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@OptIn(ExperimentalMaterial3Api::class)
class EditScreen(private val email: String): Screen {
    var oldPasswordState by mutableStateOf("")
    var newPasswordState by mutableStateOf("")
    var confirmNewPasswordState by mutableStateOf("")

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel =
            rememberScreenModel() { ProfileScreenModel(LoginRepositoryRealmLocal()) }
        val state by screenModel.state.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = { EditTopBar() },
                bottomBar = { MyBottomBar() }
            ) {
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
                        is ProfileScreenModel.State.Init -> Text("")
                        is ProfileScreenModel.State.Loading -> Text("")
                        is ProfileScreenModel.State.Result -> {
                            Text("")
                        }

                        else -> {}
                    }
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .aspectRatio(1f)
                            .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)

                    ) {
                        Image(
                            url = "https://i.pinimg.com/564x/40/9b/94/409b94c14fe4214b5b6397e637c331b9.jpg",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = "$email",
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.padding(16.dp)) {
                        Card( shape = RoundedCornerShape(10.dp),
                            elevation = 8.dp,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            backgroundColor = Color.White,) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Change Password", fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(15.dp))
                                PasswordTextField(
                                    value = oldPasswordState,
                                    onValueChange = { oldPasswordState = it },
                                    label = "Old Password"
                                )

                                PasswordTextField(
                                    value = newPasswordState,
                                    onValueChange = { newPasswordState = it },
                                    label = "New Password"
                                )

                                PasswordTextField(
                                    value = confirmNewPasswordState,
                                    onValueChange = { confirmNewPasswordState = it },
                                    label = "Confirm New Password"
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Button(
                                        onClick = {
                                            saveChanges()
                                        },
                                        modifier = Modifier.padding(10.dp),
                                        shape = RoundedCornerShape(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.White,
                                            contentColor = MaterialTheme.colors.onBackground
                                        ),
                                        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                                    ) {
                                        Text(
                                            "Save",
                                            style = MaterialTheme.typography.button,
                                            modifier = Modifier.padding(
                                                top = 8.dp,
                                                start = 20.dp,
                                                end = 20.dp,
                                                bottom = 8.dp
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Button(
                                        onClick = {
                                            val localEmail = email
                                            navigator.push(
                                                ScreenRouter(
                                                    AllScreens.Profile(
                                                        localEmail
                                                    )
                                                )
                                            )
                                        },
                                        modifier = Modifier.padding(10.dp),
                                        shape = RoundedCornerShape(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.White,

                                            contentColor = MaterialTheme.colors.onBackground
                                        ),
                                        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                                    ) {
                                        Text(
                                            "Cancel",
                                            style = MaterialTheme.typography.button,
                                            modifier = Modifier.padding(
                                                top = 8.dp,
                                                start = 20.dp,
                                                end = 20.dp,
                                                bottom = 8.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                    }
                    }
                }
            }
        }



    @Composable
    private fun PasswordTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    private fun saveChanges() {
        val oldPassword = oldPasswordState
        val newPassword = newPasswordState
        val confirmNewPassword = confirmNewPasswordState

        // Perform saving logic here
        // Validate the inputs and handle the password change
    }
}