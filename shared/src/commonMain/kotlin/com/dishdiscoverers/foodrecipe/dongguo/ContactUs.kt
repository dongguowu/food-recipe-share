package com.dishdiscoverers.foodrecipe.dongguo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepositoryTheMealAPIJson
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserFavoriteRecipeRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeCommentRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.screenModel.RecipeScreenModel
import com.dishdiscoverers.foodrecipe.xiaowei.MyBottomBar
import io.github.aakira.napier.Napier

class ContactUsScreen(val email: String? = "dongguo@wu.com") : Screen {

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

        var message by remember { mutableStateOf(email ?: "") }



        // Layout - Scaffold
        Scaffold(
            topBar = { Text(message) },
            bottomBar = {
                MyBottomBar(email)
            },

            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {

                }
            },
        )
    }
}


