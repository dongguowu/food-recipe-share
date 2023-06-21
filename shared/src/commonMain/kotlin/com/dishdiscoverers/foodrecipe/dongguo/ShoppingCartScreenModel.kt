package com.lduboscq.appkickstarter.main.screenModel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.dongguo.data.CartLine
import com.dishdiscoverers.foodrecipe.dongguo.data.CartLineData
import com.dishdiscoverers.foodrecipe.dongguo.data.ShoppingCartRepositoryInterface
import com.dishdiscoverers.foodrecipe.dongguo.data.BookData
import com.dishdiscoverers.foodrecipe.dongguo.data.BookRepositoryInterface
import kotlinx.coroutines.launch

class ShoppingCartScreenModel(
    private val shoppingCartRepository: ShoppingCartRepositoryInterface,
    private val bookRepository: BookRepositoryInterface
) :
    StateScreenModel<ShoppingCartScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val cartLineList: List<CartLine>) : State()
        //TODO: singleResult and multipleResult
    }

    fun getAllBook(): List<BookData> {
        return bookRepository.getAll()
    }

    fun searchBook(title: String): List<BookData> {
        return bookRepository.findByTitle(title)
    }

    fun getCartLineByBookId(bookId: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(cartLineList = shoppingCartRepository.getByBookId(bookId))
        }
    }


    fun addOrUpdateCartLine(cartLine: CartLineData) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(cartLineList = shoppingCartRepository.addOrUpdate(cartLine))
        }
    }

    fun deleteCartLineByBookId(bookId: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result(cartLineList = shoppingCartRepository.delete(bookId))
        }
    }


}
