package com.lduboscq.appkickstarter.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.dongguo.data.BookData
import com.lduboscq.appkickstarter.main.router.Route
import com.dishdiscoverers.foodrecipe.dongguo.layout.MyBottomBar
import com.lduboscq.appkickstarter.main.view.component.Image


internal class DetailScreen(var book: BookData?) : Screen {


    @Composable
    override fun Content() {
        val quantity = 0

        Scaffold(
            topBar = { },
            bottomBar = {
                MyBottomBar(
                    quantity = quantity,
                    currentScreen = Route.Detail(null),
                    showCart = false
                )
            },

            content = { paddingValues ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        Modifier.padding(horizontal = 14.dp).fillMaxSize()
                    ) {

                        LazyColumn(
                            modifier = Modifier.padding(0.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            item {
                                book?.let {
                                    Image(
                                        url = it.imagePath,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            var array = book?.description?.split("\n") ?: emptyList()
                            for (text in array) {
                                item {
                                    Text(
                                        text = text,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 24.sp,
                                        lineHeight = 42.sp,
                                        textAlign = TextAlign.Start,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                            for (i in 1..5) {
                                item {
                                    Text("")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}