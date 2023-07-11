package com.dishdiscoverers.foodrecipe.xiaowei

import Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow


@Composable
fun MyBottomBar(email: String? = "", currentScreen: AllScreens? = null) {
    val navigator = LocalNavigator.currentOrThrow
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 10.dp, // Add elevation to the BottomAppBar for a raised appearance
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 8.dp
        ), // Adjust the padding as per your needs

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    navigator.push(ScreenRouter(AllScreens.Home(email = null)))
                },
                enabled = currentScreen !is AllScreens.Home
            ) {
                Image(
                    url = "https://icons.iconarchive.com/icons/custom-icon-design/pretty-office-4/256/home-icon.png",
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = {
                    navigator.push(ScreenRouter(AllScreens.PreLogin))
                },
                enabled = currentScreen !is AllScreens.PreLogin
            ) {
                Image(
                    url = "https://cdn-icons-png.flaticon.com/512/1041/1041373.png",
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }



            IconButton(
                onClick = {
                    navigator.push(ScreenRouter(AllScreens.PreLogin))
                },
                enabled = currentScreen !is AllScreens.PreLogin

            ) {
                Image(
                    url = "https://cdn-icons-png.flaticon.com/512/985/985478.png",
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }



            IconButton(
                onClick = {
                    navigator.push(ScreenRouter(AllScreens.Profile(email ?: null)))
                },
                enabled = currentScreen !is AllScreens.Profile
            ) {
                Image(
                    url = "https://cdn.iconscout.com/icon/free/png-256/free-profile-417-1163876.png",
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

    }
}