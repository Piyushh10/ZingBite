package com.example.ZingBite.ui.navigation
import kotlinx.serialization.Serializable


interface NavRoute

@Serializable
object Login : NavRoute

@Serializable
object SignUp : NavRoute

@Serializable
object AuthScreen : NavRoute

@Serializable
object Home : NavRoute

@Serializable
data class RestaurantDetails(
    val restaurantId: String,
    val restaurantName: String,
    val restaurantImageUrl: String,
) : NavRoute

//@Serializable
//data class FoodDetails(val foodItem: FoodItem) : NavRoute

@Serializable
object Cart : NavRoute

@Serializable
object Notification : NavRoute

@Serializable
object AddressList : NavRoute

@Serializable
object AddAddress : NavRoute

@Serializable
data class OrderSuccess(val orderId: String) : NavRoute

@Serializable
data class OrderDetails(val orderId: String) : NavRoute

@Serializable
object OrderList : NavRoute

@Serializable
object MenuList : NavRoute

@Serializable
object AddMenu : NavRoute

@Serializable
object ImagePicker : NavRoute