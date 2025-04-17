package com.example.pawpal


import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.List


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color =MaterialTheme.colorScheme.background) {
                    PetApp()
//                    PostPetScreen()
//                    ApplyForm()
                }
//                PostPetScreen()
//                PetRecordScreen()
//                CountryListApp()
//                AddPetScreen() { }
//                PetRecordInputScreen()
//                PetDetail()
//                PetApp()
            }
        }
    }
}


// 定义导航项
sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object AvailablePets : Screen("availablePets", "Available Pets", { Icon(Icons.Filled.Home, contentDescription = "Available Pets") })
    object Explore : Screen("explore", "Explore", { Icon(Icons.Filled.Search, contentDescription = "Explore") })
    object MyProfile : Screen("myProfile", "My Profile", { Icon(Icons.Filled.Person, contentDescription = "My Profile") })
    // 注意：SavedPets不是底部导航栏的一部分，而是从AvailablePets可以访问的页面
    object SavedPets : Screen("savedPets", "Saved Pets", { Icon(Icons.Filled.Favorite, contentDescription = "Saved Pets") })
    // 添加Report页面路由
    object PostPet : Screen("PostPet", "PostPet", {})
    object AdoptionStats : Screen("adoptionStats", "Adoption Stats", { Icon(Icons.Filled.List, contentDescription = "Adoption Stats") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetApp() {
    val navController = rememberNavController()

    // 定义导航项列表（仅底部导航栏的项目）
    val items = listOf(
        Screen.AvailablePets,
        Screen.Explore,
        Screen.MyProfile
    )

    // 获取当前导航状态
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // 只在指定的三个主页面显示底部导航栏
            val bottomNavRoutes = listOf(
                Screen.AvailablePets.route,
                Screen.Explore.route,
                Screen.MyProfile.route
            )

            if (currentDestination?.route in bottomNavRoutes) {
                NavigationBar {
                    items.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        NavigationBarItem(
                            icon = { screen.icon() },
                            label = { Text(screen.label) },
                            selected = selected,
                            onClick = {
                                // 避免重复导航到当前页面
                                if (!selected) {
                                    navController.navigate(screen.route) {
                                        // 弹出到起始页面，避免导航栈无限增长
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // 避免在返回堆栈中创建多个导航项实例
                                        launchSingleTop = true
                                        // 在导航项之间切换时恢复状态
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.AvailablePets.route,
            modifier = Modifier.padding(padding)
        ) {
            // Available Pets页面
            composable(Screen.AvailablePets.route) {
                AvailablePetsScreen(
                    onSavedPetsClick = {
                        navController.navigate(Screen.SavedPets.route)
                    },
                    onPostPetClick = {
                        navController.navigate(Screen.PostPet.route)
                    }
                )
            }

            // Saved Pets页面
            composable(Screen.SavedPets.route) {
                SavedPetsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPetClick = {
                        // 这里可以处理导航到宠物详情页
                    }
                )
            }

            // Explore页面
            composable(Screen.Explore.route) {
                ExploreScreen(
                    onAdoptionProcessClick = {
                        // 这里可以导航到收养流程页面
                    },
                    onPetCareGuideClick = {
                        // 这里可以导航到宠物护理指南页面
                    },
                    onAdoptionStatReportClick = {
                        // 导航到报告页面
                        navController.navigate(Screen.AdoptionStats.route)
                    }
                )
            }

            // 报告页面
            composable(Screen.AdoptionStats.route) {
                ReportScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // My Profile页面
            composable(Screen.MyProfile.route) {
                ProfileScreen(
                    onSaveProfile = {
                        // 处理保存个人资料的逻辑
                        navController.popBackStack()
                    },
                    onSettingsClick = {
                        // 处理设置点击事件
                    }
                )
            }

            composable(Screen.PostPet.route) {
                PostPetScreen()
            }
        }
    }
}


// 临时的Available Pets屏幕，未来会被真实实现替代
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailablePetsScreen(onSavedPetsClick: () -> Unit, onPostPetClick : () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Pets") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // 添加右上角的收藏按钮
                    IconButton(onClick = onSavedPetsClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Saved Pets",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onPostPetClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Post Pet",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Available Pets屏幕的内容
        Text(
            text = "List of Available Pets will appear here",
            modifier = Modifier
                .padding(paddingValues)  // 只使用一个padding调用
        )
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun calculateAge(birthday: String): Int {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthDate = formatter.parse(birthday)
        val today = Calendar.getInstance().time
        val diff = today.time - birthDate.time
        (diff / (1000L * 60 * 60 * 24 * 365)).toInt()
    } catch (e: Exception) {
        0
    }
}

//data class Pet(
//    val name: String,
//    val type: String,
//    val breed: String,
//    val gender: String,
//    val birthday: String,
//    val imageUri: Uri?
//)

//data class PetList(val name: String, val age: String, val picture: Int)

//@Composable
//fun PetList() {
//    // flag images were downloaded and added to drawable with these names
//    val pets = remember {
//        mutableStateListOf(
//            PetList("Mia", "1 year 2 months", R.drawable.usa_flag),
//            PetList("Jacky", "2 years", R.drawable.ca_flag),
//        )
//    }
//
//    Column(Modifier.fillMaxSize().padding(top = 56.dp, start = 32.dp, end = 32.dp, bottom = 16.dp)) {
//        Row(
//            Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text("My Pet", style = MaterialTheme.typography.headlineSmall)
//
//            Icon(Icons.Default.Add, contentDescription = "Add Pet")
//
//        }
//
//        LazyColumn {
//            items(pets.size) { index ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 24.dp,)
//                ) {
//                    // Country Flag Image from drawable
//                    Image(
//                        painter = painterResource(id = pets[index].picture),
//                        contentDescription = "Flag of ${pets[index].name}",
//                        modifier = Modifier
//                            .size(40.dp)
//                            .padding(end = 8.dp)
//                    )
//
//                    // Country Name and Capital City
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(text = pets[index].name, style = MaterialTheme.typography.bodyMedium)
//                        Text(text = "Age: ${pets[index].age}", style = MaterialTheme.typography.bodyMedium)
//                    }
//
//                    // Another option to display buttons with only an icon (no label)
//                    IconButton(onClick = {
//                        pets.removeAt(index)
//                    }) {
//                        Icon(Icons.Default.Delete, contentDescription = "Delete")
//                    }
//                }
//
//                if (index < pets.size - 1) {
//                    HorizontalDivider(
//                        modifier = Modifier.fillMaxSize(),
//                        thickness = 1.dp,
//                        color= Color.LightGray
//                    )
//                }
//            }
//        }
//    }
//}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(title: String) {
    TopAppBar(
        modifier = Modifier.height(48.dp),
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}