package com.dishdiscoverers.foodrecipe.data

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.launch

class ShoppingCartScreenModel(
    private val bookRepository: BookRepositoryInterface
) :
    StateScreenModel<ShoppingCartScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<BookData>) : State()
    }

    fun getAllBook(): List<BookData> {
        return bookRepository.getAll()
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = bookRepository.getAll())
        }
    }

    fun searchBook(title: String): List<BookData> {
        return bookRepository.findByTitle(title)
        return bookRepository.getAll()
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = bookRepository.findByTitle(title))
        }
    }


}
