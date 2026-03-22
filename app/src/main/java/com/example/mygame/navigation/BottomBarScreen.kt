package com.example.mygame.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import androidx.compose.runtime.saveable.Saver
import com.example.mygame.R

@Serializable
sealed class BottomBarScreen(
    var route : String,
    var title : String,
    var icon : Int,
    var selectedIcon : Int,
    var hasNews : Boolean,
    var badgeCount : Int? = null
) : NavKey {
    @Serializable
    data object Home : BottomBarScreen(
        route = "home",
        icon = R.drawable.ic_home,
        selectedIcon = R.drawable.ic_home,
        hasNews = false,
        title = "Home"
    ){}

    @Serializable
    data object Books : BottomBarScreen(
        route = "books",
        icon = R.drawable.ic_book,
        selectedIcon = R.drawable.ic_book,
        hasNews = false,
        title = "Books",
        badgeCount = 15
    ){}

    @Serializable
    data object Profile : BottomBarScreen(
        route = "profile",
        icon = R.drawable.ic_profile,
        selectedIcon = R.drawable.ic_profile,
        hasNews = true,
        title = "Profile",
    ){}
}
val bottomBarItems = listOf(
    BottomBarScreen.Home,
    BottomBarScreen.Books,
    BottomBarScreen.Profile
)

val BottomBarScreenSaver = Saver<BottomBarScreen, String>(
    save = { BottomBarScreen.Home.route ?: "Unknown" },
    restore = {
        when (it) {
            BottomBarScreen.Home.route ->  BottomBarScreen.Home
            BottomBarScreen.Books.route -> BottomBarScreen.Books
            BottomBarScreen.Profile.route -> BottomBarScreen.Profile
            else -> BottomBarScreen.Home
        }
    }
)

