package com.example


import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId


@Introspected
data class User @BsonCreator constructor(
    @field:BsonProperty("id",useDiscriminator = false) @param:BsonProperty("_id")  val id: ObjectId?,
    @field:BsonProperty("username") @param:BsonProperty("username") val username: String,
    @field:BsonProperty("password") @param:BsonProperty("password") val password: String,
    @field:BsonProperty("email") @param:BsonProperty("email") val email: String,
    @field:BsonProperty("first_name") @param:BsonProperty("first_name") val firstName: String,
    @field:BsonProperty("last_name") @param:BsonProperty("last_name") val lastName: String,
    @field:BsonProperty("phone_number") @param:BsonProperty("phone_number") val phoneNumber: String,
    @field:BsonProperty("payment_info") @param:BsonProperty("payment_info") val paymentInfo: List<OrderHistory>,
    @field:BsonProperty("order_history") @param:BsonProperty("order_history") val orderHistory: List<OrderHistory>,
    @field:BsonProperty("roles") @param:BsonProperty("roles") val roles: List<String>
) {
    constructor() : this(ObjectId(),"", "", "", "", "", "", emptyList(), emptyList(), emptyList())
}

data class PaymentInfo @BsonCreator constructor(
    @field:BsonProperty("credit_card_number") @param:BsonProperty("credit_card_number") val creditCardNumber: String,
    @field:BsonProperty("expiration_date") @param:BsonProperty("expiration_date") val expirationDate: String,
    @field:BsonProperty("cvv") @param:BsonProperty("cvv") val cvv: String
) {
    constructor() : this("", "", "")
}

data class OrderHistory @BsonCreator constructor(
    @field:BsonProperty("idProduct") @param:BsonProperty("idProduct") val idProduct: String,
    @field:BsonProperty("product_name") @param:BsonProperty("product_name") val productName: String,
    @field:BsonProperty("purchase_date") @param:BsonProperty("purchase_date") val purchaseDate: String,
    @field:BsonProperty("order_status") @param:BsonProperty("order_status") val orderStatus: String,
    @field:BsonProperty("quantityNum") @param:BsonProperty("quantityNum") val quantityNum: Int,
    @field:BsonProperty("urlImage") @param:BsonProperty("urlImage") val urlImage: String,
) {
    constructor() : this("","", "", "",0, "")
}