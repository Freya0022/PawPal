package com.example.pawpal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.ArrowBack

// 宠物数据类
data class Pet(
    val id: Int,
    val name: String,
    val breed: String,
    val age: Int,
    val gender: String,
    val imageRes: Int,
    var isFavorite: Boolean = true // 已保存的宠物默认都是收藏状态
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPetsScreen(
    onBackClick: () -> Unit = {},
    onPetClick: (Pet) -> Unit = {}
) {
    // 示例宠物数据，实际应用中应该从数据库获取
    val savedPets = remember {
        mutableStateListOf(
            Pet(1, "Max", "Golden Retriever", 3, "Male", R.drawable.ic_launcher_foreground),
            Pet(2, "Bella", "Labrador", 2, "Female", R.drawable.ic_launcher_foreground),
            Pet(3, "Charlie", "German Shepherd", 4, "Male", R.drawable.ic_launcher_foreground),
            Pet(4, "Lucy", "Beagle", 1, "Female", R.drawable.ic_launcher_foreground),
            Pet(5, "Cooper", "Bulldog", 5, "Male", R.drawable.ic_launcher_foreground)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = { Text(text = "Saved Pets") },
                navigationIcon = {
                    // 添加返回按钮
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Available Pets"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (savedPets.isEmpty()) {
            // 空状态显示
            EmptyPetsState(modifier = Modifier.padding(paddingValues))
        } else {
            // 有数据时显示列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
//                items(savedPets) { pet ->
//                    PetItem(
//                        pet = pet,
//                        onPetClick = { onPetClick(pet) },
//                        onFavoriteToggle = { petId ->
//                            // 查找并更新宠物收藏状态
//                            val index = savedPets.indexOfFirst { it.id == petId }
//                            if (index != -1) {
//                                savedPets[index] = savedPets[index].copy(
//                                    isFavorite = !savedPets[index].isFavorite
//                                )
//
//                                // 如果取消收藏，可以选择从保存列表中移除
//                                if (!savedPets[index].isFavorite) {
//                                    savedPets.removeAt(index)
//                                }
//                            }
//                        }
//                    )
//                }
            }
        }
    }
}

@Composable
fun PetItem(
    pet: Pet,
    onPetClick: () -> Unit,
    onFavoriteToggle: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPetClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 宠物图片
            Image(
                painter = painterResource(id = pet.imageRes),
                contentDescription = "Pet image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 宠物信息
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${pet.breed} · ${pet.age} years",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Gender: ${pet.gender}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 收藏按钮
            IconButton(
                onClick = { onFavoriteToggle(pet.id) }
            ) {
                Icon(
                    imageVector = if (pet.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle favorite",
                    tint = if (pet.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyPetsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Saved Pets",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pets you save will appear here",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
