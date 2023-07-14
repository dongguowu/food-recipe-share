package com.dishdiscoverers.foodrecipe.dongguo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepositoryTheMealAPIJson
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserFavoriteRecipeRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeCommentRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.repository.api.EmailService
import com.dishdiscoverers.foodrecipe.dongguo.screenModel.RecipeScreenModel
import com.dishdiscoverers.foodrecipe.xiaowei.MyBottomBar
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ContactUsScreen(val email: String? = "dongguo@wu.com") : Screen, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {


        // Insert repository
        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                apiRepository = RecipeRepositoryTheMealAPIJson(),
                authRepository = AuthRepository(),
                commentRepository = UserRecipeCommentRepositoryFirebase(AuthRepository()),
                favoriteRepository = UserFavoriteRecipeRepositoryFirebase(AuthRepository()),
            )
        }

        var message by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }


        // Layout - Scaffold
        Scaffold(topBar = { Text(message) }, bottomBar = {
            MyBottomBar(email)
        },

            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    TextField(
                        value = subject,
                        onValueChange = {
                            subject = it
                        }
                    )
                    TextField(
                        value = content,
                        onValueChange = {
                            content = it
                        }
                    )

                    Button(onClick = {
                        Napier.i { "button clicked" }
                        launch {
                            EmailService.sendEmail(
                                subject = subject, body = content
                            )
                        }

                    }) {
                        Text(
                            "send email via sendgrid.net", fontSize = 12.sp
                        )
                    }
                }
            })
    }
}


