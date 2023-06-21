package com.dishdiscoverers.foodrecipe.dongguo.data

interface ShoppingCartRepositoryInterface {

    suspend fun getAll(): List<CartLine>
    suspend fun getByBookId(bookId: String): List<CartLine>
    suspend fun addOrUpdate(cartLineData: CartLineData): List<CartLine>
    suspend fun delete(id: String): List<CartLine>
}


data class CartLineData(
    var id: String? = null,
    var bookId: String = "",
    var quantity: Int = 1
)

data class CartLine(
    var _id: String? = null,
    var bookId: String = "",
    var quantity: Int = 1


)

class MockShoppingCartRepository : ShoppingCartRepositoryInterface {
    private val cartLines: MutableList<CartLine> = mutableListOf()

    override suspend fun getAll(): List<CartLine> {
        return cartLines
    }

    override suspend fun getByBookId(bookId: String): List<CartLine> {
        return cartLines.filter { it.bookId == bookId }
    }

    override suspend fun addOrUpdate(cartLineData: CartLineData): List<CartLine> {
        val existingCartLine = cartLines.find { it._id == cartLineData.id }

        if (existingCartLine != null) {
            existingCartLine.bookId = cartLineData.bookId
            existingCartLine.quantity = cartLineData.quantity
        } else {
            val newCartLine = CartLine(cartLineData.id, cartLineData.bookId, cartLineData.quantity)
            cartLines.add(newCartLine)
        }

        return cartLines
    }

    override suspend fun delete(id: String): List<CartLine> {
        var line = cartLines.firstOrNull() { it._id == id }
        cartLines.remove(line)
        return cartLines
    }
}

