package com.example.mygame.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.SaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.delay
import kotlin.collections.listOf
import kotlin.compareTo
import com.example.mygame.R
import com.example.mygame.navigation.NavDrawerItem.Books.NavDrawerItemSaver
import com.example.mygame.ui.screens.ExplorationScreen
import com.example.mygame.ui.screens.InventoryScreen
import com.example.mygame.ui.screens.MarketScreen
import com.example.mygame.ui.screens.StatueScreen
import com.example.mygame.viewmodel.GameViewModel
import kotlinx.coroutines.launch


@Composable
fun RootGraph(gameViewModel : GameViewModel){
    val backStack = rememberNavBackStack(Screen.Auth)
    val stateHolder = rememberSaveableStateHolder()

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            SaveableStateHolderNavEntryDecorator( saveableStateHolder = stateHolder),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider{
            entry<Screen.Auth> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Button(onClick = {
                        backStack.add(Screen.NestedGraph)
                    }) {
                        Text(text = "Sign in")
                    }
                }
            }
            entry<Screen.Setting>{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Button(onClick = {
                        backStack.removeLastOrNull()
                    }) {
                        Text(text = "Navigate back")
                    }
                }
            }
            entry<Screen.NestedGraph> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    NestedGraph(
                        gameViewModel,
                        navigateToSetting = {
                            backStack.add(Screen.Setting)
                        }
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedGraph(
    gameViewModel : GameViewModel,
    navigateToSetting : () -> Unit
){
    val backStack = rememberNavBackStack(BottomBarScreen.Home)
    val stateHolder = rememberSaveableStateHolder()
    var currentBottomBarScreen2 : BottomBarScreen by rememberSaveable (
        stateSaver = BottomBarScreenSaver
    ) {
        mutableStateOf(BottomBarScreen.Home)
    }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val currentBottomBarScreen = backStack.last() as? BottomBarScreen ?: BottomBarScreen.Home

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentBottomBarScreen.title)},
                actions = {
                    IconButton(onClick = navigateToSetting ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar{
                IconButton(onClick = {showSheet = true}) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open Menu")
                }
                bottomBarItems.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (destination.badgeCount != null) {
                                        Badge {
                                            Text(text = destination.badgeCount.toString())
                                        }
                                    } else if (destination.hasNews) {
                                        Badge()
                                    }
                                }
                            ){
                                val iconRes = if (currentBottomBarScreen == destination)
                                    destination.selectedIcon else destination.icon
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = destination.title
                                )
                            }
                        },
                        label = {Text(destination.title)},
                        selected = currentBottomBarScreen == destination,
                        onClick = {
                            if (currentBottomBarScreen != destination) {
                                backStack.removeAll { it == destination }
                                backStack.add(destination)
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavDisplay(
            modifier = Modifier.padding(it),
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryDecorators = listOf(
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<BottomBarScreen.Home> {
                    StatueScreen(gameViewModel)
                }
                entry<BottomBarScreen.Books> {
                    ExplorationScreen(gameViewModel)
                }
                entry<BottomBarScreen.Profile> {
                    InventoryScreen(gameViewModel)
                }
                entry<NavDrawerItem.Home> {
                    StatueScreen(gameViewModel)
                }
                entry<NavDrawerItem.Music> {
                    MarketScreen(gameViewModel)
                }
                entry<NavDrawerItem.Movies> {
                    MoviesScreen()
                }
                entry<NavDrawerItem.Books> {
                    BooksScreen()
                }
                entry<NavDrawerItem.Profile> {
                    ProfileScreen()
                }
                entry<NavDrawerItem.Settings> {
                    SettingsScreen()
                }
            }
        )
    }
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            // Centering the content inside the sheet
            modifier = Modifier.fillMaxWidth()
        ) {
            // We reuse your Drawer content here
            BottomDrawerContent(
                backStack = backStack,
                onItemClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showSheet = false
                    }
                }
            )
        }
    }
}

@Composable
fun BottomDrawerContent(
    backStack: NavBackStack<NavKey>,
    onItemClick: (NavDrawerItem) -> Unit
) {
    val items = listOf(
        NavDrawerItem.Home,
        NavDrawerItem.Music,
        NavDrawerItem.Movies,
        NavDrawerItem.Books,
        NavDrawerItem.Profile,
        NavDrawerItem.Settings
    )

    var currentItem by rememberSaveable(stateSaver = NavDrawerItemSaver) {
        mutableStateOf(NavDrawerItem.Home)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp), // Extra space at bottom for gestures
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo or Header
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(60.dp).padding(16.dp)
        )

        items.forEach { item ->
            DrawerItem(
                item = item,
                selected = item == currentItem,
                onItemClick = {
                    backStack.add(item)
                    currentItem = item
                    onItemClick(item) // Closes the sheet
                }
            )
        }
    }
}

@Composable
fun DrawerItem(
    item: NavDrawerItem,
    selected: Boolean,
    onItemClick: (NavDrawerItem) -> Unit
) {
    val background = if (selected) R.color.purple_700 else android.R.color.transparent
    val foreground = if (selected) Color.White else Color.Black

    NavigationDrawerItem(
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .height(45.dp)
                    .background(colorResource(id = background))
                    .padding(start = 10.dp)

            ){
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    colorFilter = ColorFilter.tint(foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(35.dp)
                        .width(35.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    color = foreground,
                )
            }
        },
        selected = selected,
        onClick = { onItemClick(item) }
    )
}
@Composable
fun DrawerNavigator(backStack: NavBackStack<NavKey>, modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            //rememberSavedStateNavEntryDecorator(),
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavDrawerItem.Home> {
                HomeScreen()
            }
            entry<NavDrawerItem.Music> {
                MusicScreen()
            }
            entry<NavDrawerItem.Movies> {
                MoviesScreen()
            }
            entry<NavDrawerItem.Books> {
                BooksScreen()
            }
            entry<NavDrawerItem.Profile> {
                ProfileScreen()
            }
            entry<NavDrawerItem.Settings> {
                SettingsScreen()
            }

        }
    )
}