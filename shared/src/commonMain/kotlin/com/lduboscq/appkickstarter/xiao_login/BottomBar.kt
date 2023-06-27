package com.lduboscq.appkickstarter.xiao_login

import Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lduboscq.appkickstarter.xiao_login.ui.md_theme_dark_primary


@Composable
fun MyBottomBar() {
    val navigator = LocalNavigator.currentOrThrow
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = md_theme_dark_primary
    ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {
                    navigator.push(ScreenRouter(AllScreens.PreLogin))
                }) {
                    Image(
                        url = "https://icons.iconarchive.com/icons/custom-icon-design/pretty-office-4/256/home-icon.png",
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }



                IconButton(onClick = {
                    navigator.push(ScreenRouter(AllScreens.PreLogin))
                }) {
                    Image(
                        url = "https://cdn-icons-png.flaticon.com/512/1041/1041373.png",
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }



                IconButton(onClick = {
                    navigator.push(ScreenRouter(AllScreens.PreLogin))
                }) {
                    Image(
                        url = "https://cdn-icons-png.flaticon.com/512/985/985478.png",
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }



                IconButton(onClick = {
                    navigator.push(ScreenRouter(AllScreens.PreLogin))
                }) {
                    Image(
                        url = "https://static.vecteezy.com/system/resources/previews/010/056/184/original/people-icon-sign-symbol-design-free-png.png",
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

    }
}