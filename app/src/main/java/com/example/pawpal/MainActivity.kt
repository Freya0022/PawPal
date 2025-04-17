package com.example.pawpal

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController
import com.example.pawpal.Pet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 🔥 Move this to top globally
val savedPets = mutableStateListOf(
    Pet(1, "Max", "Golden Retriever", 3, "Male", R.drawable.bella),
    Pet(2, "Bella", "Labrador", 2, "Female", R.drawable.bella),
    Pet(3, "Charlie", "German Shepherd", 4, "Male", R.drawable.bella),
    Pet(4, "Lucy", "Beagle", 1, "Female", R.drawable.bella),
    Pet(5, "Cooper", "Bulldog", 5, "Male", R.drawable.bella)
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PetApp()
                }
            }
        }
    }
}

// Define Screens
sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object AvailablePets : Screen("availablePets", "Available Pets", { Icon(Icons.Filled.Home, contentDescription = "Available Pets") })
    object Explore : Screen("explore", "Explore", { Icon(Icons.Filled.Search, contentDescription = "Explore") })
    object MyProfile : Screen("myProfile", "My Profile", { Icon(Icons.Filled.Person, contentDescription = "My Profile") })
    object SavedPets : Screen("savedPets", "Saved Pets", { Icon(Icons.Filled.Favorite, contentDescription = "Saved Pets") })
    object PostPet : Screen("PostPet", "Post Pet", { })
    object AdoptionStats : Screen("adoptionStats", "Adoption Stats", { Icon(Icons.Filled.List, contentDescription = "Adoption Stats") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetApp() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.AvailablePets,
        Screen.Explore,
        Screen.MyProfile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
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
                                if (!selected) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
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
            composable(Screen.AvailablePets.route) {
                AvailablePetsScreen(
                    navController = navController,
                    onSavedPetsClick = { navController.navigate(Screen.SavedPets.route) },
                    onPostPetClick = { navController.navigate(Screen.PostPet.route) }
                )
            }
            composable(Screen.SavedPets.route) {
                SavedPetsScreen(
                    onBackClick = { navController.popBackStack() },
                    onPetClick = { /* Navigate to pet details */ }
                )
            }
            composable(Screen.Explore.route) {
                ExploreScreen(
                    onAdoptionProcessClick = { /* TBD */ },
                    onPetCareGuideClick = { /* TBD */ },
                    onAdoptionStatReportClick = { navController.navigate(Screen.AdoptionStats.route) }
                )
            }
            composable(Screen.AdoptionStats.route) {
                ReportScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.MyProfile.route) {
                ProfileScreen(
                    onSaveProfile = { navController.popBackStack() },
                    onSettingsClick = { /* TBD */ }
                )
            }
            composable(Screen.PostPet.route) {
                PostPetScreen()
            }
            composable("petDetail/{petId}") { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId")?.toIntOrNull()
                val pet = savedPets.find { it.id == petId }
                if (pet != null) {
                    PetDetailScreen(navController, pet)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailablePetsScreen(navController: NavController, onSavedPetsClick: () -> Unit, onPostPetClick: () -> Unit) {
    var selectedBreed by remember { mutableStateOf("All") }
    var selectedGender by remember { mutableStateOf("All") }

    val breedOptions = listOf("All") + savedPets.map { it.breed }.distinct()
    val genderOptions = listOf("All") + savedPets.map { it.gender }.distinct()

    val filteredPets = savedPets.filter {
        (selectedBreed == "All" || it.breed == selectedBreed) &&
                (selectedGender == "All" || it.gender == selectedGender)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Pets") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onSavedPetsClick) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Saved Pets", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    IconButton(onClick = onPostPetClick) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Post Pet", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    DropdownSelector(
                        label = "Breed",
                        options = breedOptions,
                        selectedOption = selectedBreed,
                        onOptionSelected = { selectedBreed = it }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    DropdownSelector(
                        label = "Gender",
                        options = genderOptions,
                        selectedOption = selectedGender,
                        onOptionSelected = { selectedGender = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredPets) { pet ->
                    PetCard(
                        pet = pet,
                        onClick = {
                            navController.navigate("petDetail/${pet.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun PetCard(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pet.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Breed: ${pet.breed}")
            Text(text = "Age: ${pet.age}")
            Text(text = "Gender: ${pet.gender}")
        }
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
