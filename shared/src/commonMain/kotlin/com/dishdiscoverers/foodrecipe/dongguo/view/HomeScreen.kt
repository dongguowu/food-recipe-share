package com.lduboscq.appkickstarter.main.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dishdiscoverers.foodrecipe.dongguo.data.BookData
import com.dishdiscoverers.foodrecipe.dongguo.data.BookRepositoryLocalList
import com.dishdiscoverers.foodrecipe.dongguo.data.CartLine
import com.dishdiscoverers.foodrecipe.dongguo.data.CartLineData
import com.dishdiscoverers.foodrecipe.dongguo.data.MockShoppingCartRepository
import com.dishdiscoverers.foodrecipe.dongguo.layout.MyBottomBar
import com.dishdiscoverers.foodrecipe.dongguo.layout.MyTopBar
import com.dishdiscoverers.foodrecipe.dongguo.model.DongguoUser
import com.lduboscq.appkickstarter.main.router.Route
import com.lduboscq.appkickstarter.main.router.screenRouter
import com.lduboscq.appkickstarter.main.screenModel.ShoppingCartScreenModel
import com.lduboscq.appkickstarter.main.view.component.AddOrSubstrateQuantity
import com.lduboscq.appkickstarter.main.view.component.Image

internal class BookStoreHomeScreen(var user: DongguoUser? = null) : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert shopping cart repository
        val screenModel = rememberScreenModel() {
            ShoppingCartScreenModel(
                shoppingCartRepository = MockShoppingCartRepository(),
                bookRepository = BookRepositoryLocalList()
            )
        }
        val state by screenModel.state.collectAsState()


        // Local static books data
        var bookList = screenModel.getAllBook()
        var bookListState = remember {
            bookList.toMutableStateList()
        }
        LaunchedEffect(Unit) {
        }

        // Message
        var messageOnTopBar by remember { mutableStateOf("") }
        when (val result = state) {
            is ShoppingCartScreenModel.State.Init -> messageOnTopBar = "Just initialized"
            is ShoppingCartScreenModel.State.Loading -> messageOnTopBar = "Loading"
            is ShoppingCartScreenModel.State.Result -> messageOnTopBar = "Success"
            else -> {}
        }

        // Load shopping cart data
        LaunchedEffect(true) {
            screenModel.getCartLineByBookId("")
        }

        var quantity by remember { mutableStateOf(0) }
        if (state is ShoppingCartScreenModel.State.Result) {
            quantity =
                (state as ShoppingCartScreenModel.State.Result).cartLineList.sumOf { item -> item.quantity }
        }

        if (user != null) {
            messageOnTopBar = "hi, ${user?.name}"
        }


        // Layout - Scaffold
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            topBar = { MyTopBar(messageOnTopBar, scrollBehavior) },

            bottomBar = { MyBottomBar(quantity = quantity, currentScreen = Route.Home(user)) },

            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    var text by rememberSaveable { mutableStateOf("") }
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                            if (it.length >= 3) {
                                messageOnTopBar = "searching book $it"
                                val filteredList = screenModel.searchBook(it)
                                if (filteredList.size >= 0) {
                                    messageOnTopBar = "found ${filteredList.size} book(s) "
                                    bookListState.clear()
                                    for (item in filteredList) {
                                        bookListState.add(item)
                                    }
                                } else {
                                    messageOnTopBar = "not found book on $it"
                                }
                            } else {
                                messageOnTopBar = ""
                                bookListState.clear()
                                for (item in screenModel.getAllBook()) {
                                    bookListState.add(item)
                                }
                            }

                        },
                        label = {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = "Search books",
                            )
                        }
                    )

                    // Books list
                    LazyColumn {
                        for (book in bookListState) {
                            item {
                                var cartLineList: List<CartLine>? = null
                                if (state is ShoppingCartScreenModel.State.Result) {
                                    cartLineList =
                                        (state as ShoppingCartScreenModel.State.Result).cartLineList
                                }
                                BookCard(
                                    book = book,
                                    cartLine = cartLineList?.firstOrNull { it.bookId == book.id },
                                    addToCartOrUpdate = { screenModel.addOrUpdateCartLine(it) },
                                    removeFromCat = { screenModel.deleteCartLineByBookId(it) })
                            }
                        }
                    }
                }
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        )
    }
}


/**

Represents a card component for displaying book information including title,
picture and favorite icon button , add to shopping cart icon button.
@param book The book object to display.
@param addToCart A callback function to handle adding the book to the shopping cart.
 */
@Composable
fun BookCard(
    book: BookData,
    cartLine: CartLine?,
    addToCartOrUpdate: (cartLineData: CartLineData) -> Unit,
    removeFromCat: (cartLineId: String) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        modifier = Modifier.size(width = 400.dp, height = 200.dp).padding(15.dp),
    ) {
        Row {
            Image(
                url = book.imagePath,
                modifier = Modifier.size(width = 120.dp, height = 180.dp).padding(15.dp)
                    .clickable(onClick = {
                        navigator.push(screenRouter(Route.Detail(book)))
                    })
            )
            Column(
                modifier = Modifier.padding(9.dp, 15.dp, 9.dp, 9.dp),
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(60.dp).width(60.dp))

                Row {
                    // Favorite icon
                    var checked by remember { mutableStateOf(false) }
                    IconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                        if (checked) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                Icons.Outlined.Favorite,
                                contentDescription = "Localized description"
                            )
                        }
                    }


                    // add shopping icon
                    if (cartLine != null) {
                        AddOrSubstrateQuantity(cartLine = cartLine,
                            update = { addToCartOrUpdate(it) },
                            delete = { removeFromCat(it) })
                    } else {


                        SmallFloatingActionButton(
                            onClick = {
                                addToCartOrUpdate(CartLineData(bookId = book.id, quantity = 1))
                            }
                        ) {
                            Icon(
                                Icons.Outlined.ShoppingCart,
                                contentDescription = "Localized description",
                            )
                        }

                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBook(filterBookList: (String) -> Unit, updateMessageOnTopBar: (String) -> Unit) {
    var queryBookstring = ""

    OutlinedTextField(
        value = queryBookstring,
        onValueChange = {
//            queryBookstring = it
            if (it.length >= 3) {
                filterBookList(it)
                updateMessageOnTopBar("Finding book $it")
            }
        },
        label = {
            Icon(
                Icons.Outlined.Search,
                contentDescription = "Search books",
            )
        }
    )
}
