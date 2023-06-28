package com.dishdiscoverers.foodrecipe.xiaowei
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@OptIn(ExperimentalMaterial3Api::class)
class EditScreen : Screen {
    var oldPasswordState by mutableStateOf("")
    var newPasswordState by mutableStateOf("")
    var confirmNewPasswordState by mutableStateOf("")

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Card(modifier = Modifier.fillMaxWidth(), elevation = 8.dp) {
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { saveChanges() },
                            ) {
                                Text("Save")
                            }

                            Spacer(modifier = Modifier.width(15.dp))

                            Button(
                                onClick = {
                                    navigator.push(ScreenRouter(AllScreens.Edit))
                                },
                            ) {
                                Text("Cancel")
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