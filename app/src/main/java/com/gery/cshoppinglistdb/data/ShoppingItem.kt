package com.gery.cshoppinglistdb.data

data class ShoppingItem(
    var name: String,
    var description: String,
    var price: Int,
    var category: Int,
    var status: Boolean
)