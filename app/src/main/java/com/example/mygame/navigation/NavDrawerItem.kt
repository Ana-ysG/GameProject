package com.example.mygame.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import androidx.compose.runtime.saveable.Saver
import com.example.mygame.R


@Serializable
sealed class NavDrawerItem(var route : String, var icon : Int, var title: String): NavKey {
    @Serializable
    data object Home : NavDrawerItem("home", R.drawable.ic_home, "Home")
    @Serializable
    data object Music : NavDrawerItem("music", R.drawable.ic_music, "Music")
    @Serializable
    data object Movies : NavDrawerItem("movies", R.drawable.ic_movie, "Movies")
    @Serializable
    data object Books : NavDrawerItem("books", R.drawable.ic_book, "Books")
    @Serializable
    data object Profile : NavDrawerItem("profile", R.drawable.ic_profile, "Profile")
    @Serializable
    data object Settings : NavDrawerItem("settings", R.drawable.ic_settings, "Settings")

    val NavDrawerItemSaver = Saver<NavDrawerItem, String>(
        save = { it.route ?: "Unknown" },
        restore = {
            when (it) {
                NavDrawerItem.Home.route -> NavDrawerItem.Home
                NavDrawerItem.Music.route -> NavDrawerItem.Music
                NavDrawerItem.Movies.route -> NavDrawerItem.Movies
                NavDrawerItem.Books.route -> NavDrawerItem.Books
                NavDrawerItem.Profile.route -> NavDrawerItem.Profile
                NavDrawerItem.Settings.route -> NavDrawerItem.Settings
                else -> NavDrawerItem.Home
            }
        }
    )

}