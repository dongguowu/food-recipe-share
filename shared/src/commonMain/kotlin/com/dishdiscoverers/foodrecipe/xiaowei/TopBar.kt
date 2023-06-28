package com.dishdiscoverers.foodrecipe.xiaowei

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import dev.icerock.moko.resources.compose.stringResource


@Composable
fun TopBar( email: String){
    val navigator = LocalNavigator.currentOrThrow
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getShinyAppName(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    letterSpacing = 0.05.em,
                    fontStyle = FontStyle.Italic,
                    color = Color.DarkGray
                )
            )
            Box(modifier = Modifier.padding(end = 16.dp)) {
                EditIcon(
                    icon = Icons.Default.Edit,
                    tint = Color.Black,
                    onClick = {
                    navigator.push(ScreenRouter(AllScreens.Edit(email)))
                })
            }
        }
    }
}

@Composable
fun EditIcon(icon: ImageVector, tint: Color, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = null, tint = tint)
    }
}
@Composable
fun getShinyAppName(): AnnotatedString {
    val appName = "Food Recipe Share"
    val shinyAppName = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,

                letterSpacing = 0.05.em,
                fontStyle = FontStyle.Italic
            )
        ) {
            append("Food ")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,

                fontSize = 24.sp,
                letterSpacing = 0.05.em,
                fontStyle = FontStyle.Italic
            )
        ) {
            append("Recipe")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,

                fontSize = 24.sp,
                letterSpacing = 0.05.em,
                fontStyle = FontStyle.Italic
            )
        ) {
            append(" Share")
        }
    }
    return shinyAppName
}