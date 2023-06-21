package com.lduboscq.appkickstarter.main.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishdiscoverers.foodrecipe.dongguo.data.CartLine
import com.dishdiscoverers.foodrecipe.dongguo.data.CartLineData

@Composable
fun AddOrSubstrateQuantity(
    cartLine: CartLine,
    update: (cartLine: CartLineData) -> Unit,
    delete: (cartLineId: String) -> Unit,
    showRemove: Boolean = false
) {
    Row {
        SmallFloatingActionButton(
            onClick = {
                update(
                    CartLineData(
                        bookId = cartLine.bookId,
                        quantity = cartLine.quantity + 1
                    )
                )
            }) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = "Localized description",
            )
        }
        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = "${cartLine?.quantity?.toString()}",
            modifier = Modifier.padding(6.dp, 0.dp, 6.dp, 0.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.width(18.dp))
        SmallFloatingActionButton(
            onClick = {
                var quantityToUpdate = cartLine.quantity - 1
                if (quantityToUpdate <= 0) {
                    delete(cartLine._id.toString())
                } else {
                    update(CartLineData(bookId = cartLine.bookId, quantity = quantityToUpdate))
                }
            }
        ) {
            Icon(
                Icons.Outlined.Edit,
                contentDescription = "Localized description",
            )
        }
        if (showRemove) {
            Spacer(modifier = Modifier.width(18.dp))
            SmallFloatingActionButton(
                onClick = {
                    delete(cartLine._id.toString())
                }
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete the item on cart",
                )
            }
        }

    }
}