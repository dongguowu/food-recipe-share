import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.lduboscq.appkickstarter.ui.generateImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
internal fun Image(url: String, modifier: Modifier = Modifier,
                   contentDescription: String? = null,
                   alignment: Alignment = Alignment.Center,
                   contentScale: ContentScale = ContentScale.Fit,
                   alpha: Float = DefaultAlpha,
                   colorFilter: ColorFilter? = null
) {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
    ) {
        androidx.compose.foundation.Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}