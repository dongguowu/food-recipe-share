package xiao_login

import Image
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow


import kotlinx.coroutines.delay

class PreLoginScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val images = listOf(
            "https://i.pinimg.com/564x/94/74/6b/94746b53477da9d3828ba06e2473f4e4.jpg",
            "https://i.pinimg.com/564x/06/63/35/06633580042be8404a4862b8a61e8b2f.jpg",
            "https://i.pinimg.com/564x/5f/ea/4a/5fea4adff315b2724936eb5ec217a7d9.jpg",
            "https://i.pinimg.com/564x/b9/2a/d9/b92ad9278b8aed3fb0148eb7c7a4a3c7.jpg",
            "https://i.pinimg.com/564x/b3/cc/65/b3cc65036266985ddfc88f3a9e4211a0.jpg"
        )
        var currentIndex by remember { mutableStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()) {
            ImageSlider(images, currentIndex)
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    DotsIndicator(
                        itemCount = images.size,
                        currentIndex = currentIndex,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
//                            navigator.push(ScreenRouter(AllScreens.Login))
                        },
                        modifier = Modifier.padding(10.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
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
                }

            }


            LaunchedEffect(currentIndex) {
                while (true) {
                    delay(3000) // Delay between image transitions
                    currentIndex = (currentIndex + 1) % images.size
                }
            }
        }
    }

//@Composable
//fun AnimatedBackgroundImages(images: List<String>) {
//    var currentIndex by remember { mutableStateOf(0) }
//
//    val currentImage by remember(images, currentIndex) {
//        mutableStateOf(images[currentIndex])
//    }
//
//    val transition = updateTransition(currentImage, label = "backgroundImageTransition")
//    val imageModifier = Modifier.fillMaxSize()
//
//    val alpha by transition.animateFloat(
//        transitionSpec = { tween(durationMillis = 1000) }
//    ) { image ->
//        if (image == currentImage) 1f else 0f
//    }
//
//    Box(modifier = imageModifier) {
//        Image(
//            url = currentImage,
//            contentDescription = null,
//            modifier = imageModifier.alpha(alpha).fillMaxSize(),
//            contentScale = ContentScale.FillBounds,
//        )
//    }
//
//    // Animate to the next image
//    LaunchedEffect(currentIndex) {
//        delay(3000) // Delay between image transitions
//        currentIndex = (currentIndex + 1) % images.size
//    }
//}

    @Composable
    fun ImageSlider(images: List<String>, currentIndex: Int) {
        Box(modifier = Modifier.fillMaxSize()) {
            images.forEachIndexed { index, imageUrl ->
                ImageBackground(imageUrl, index, currentIndex)
            }
        }
    }

    @Composable
    fun ImageBackground(imageUrl: String, imageIndex: Int, currentIndex: Int) {
        val offsetX =
            (imageIndex - currentIndex) * 1500f // Adjust the value to control the speed of the movement

        val imageModifier = Modifier.fillMaxSize().offset(x = offsetX.dp)

        Image(
            url = imageUrl,
            modifier = imageModifier,
            contentScale = ContentScale.FillBounds
        )
    }

    @Composable
    fun DotsIndicator(
        itemCount: Int,
        currentIndex: Int,
        modifier: Modifier = Modifier,
    ) {
        val dotSpacing = 8.dp
        Row(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(itemCount) { index ->
                val dotColor = if (index == currentIndex) {
                    Color.White // Active dot color
                } else {
                    Color.LightGray // Inactive dot color
                }

                Spacer(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = dotColor, shape = CircleShape)
                )
                if (index < itemCount - 1) {
                    Spacer(modifier = Modifier.width(dotSpacing))
                }
            }
        }
    }

}
