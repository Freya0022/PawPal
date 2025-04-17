package com.example.pawpal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TimePicker
import androidx.compose.ui.text.input.ImeAction
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSaveProfile: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // 用户信息状态
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var homeType by remember { mutableStateOf("") }
    var hasOtherPets by remember { mutableStateOf(false) }
    var otherPetsDescription by remember { mutableStateOf("") }
    var hasAdoptedBefore by remember { mutableStateOf(false) }
    var adoptionReason by remember { mutableStateOf("") }
    var agreementChecked by remember { mutableStateOf(false) }

    // 年龄组选项
    val ageGroups = listOf("<18", "18-25", "26-35", "36-50", "50+")

    // 住宅类型选项
    val homeTypes = listOf("Apartment", "House with yard", "Shared accommodation", "Other")

    // 日期和时间选择器状态
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    // 展开状态
    var isAgeGroupExpanded by remember { mutableStateOf(false) }
    var isHomeTypeExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // 用户信息表单
            Text(
                text = "Personal Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

// Email Address
            OutlinedTextField(
                value = "example@email.com", // Fixed email address
                onValueChange = { }, // Empty lambda to prevent changes
                label = { Text("Email Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                readOnly = true, // Make the field read-only
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Phone Number
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            // Age - Dropdown Menu
            ExposedDropdownMenuBox(
                expanded = isAgeGroupExpanded,
                onExpandedChange = { isAgeGroupExpanded = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Age Group") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAgeGroupExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isAgeGroupExpanded,
                    onDismissRequest = { isAgeGroupExpanded = false }
                ) {
                    ageGroups.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                ageGroup = option
                                isAgeGroupExpanded = false
                            }
                        )
                    }
                }
            }

            // Occupation
            OutlinedTextField(
                value = occupation,
                onValueChange = { occupation = it },
                label = { Text("Occupation") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Home Type - Dropdown Menu
            ExposedDropdownMenuBox(
                expanded = isHomeTypeExpanded,
                onExpandedChange = { isHomeTypeExpanded = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = homeType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Home Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isHomeTypeExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isHomeTypeExpanded,
                    onDismissRequest = { isHomeTypeExpanded = false }
                ) {
                    homeTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                homeType = option
                                isHomeTypeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Do you have other pets? - Radio Buttons
            Text(
                text = "Do you have other pets?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = hasOtherPets,
                    onClick = { hasOtherPets = true }
                )
                Text("Yes", modifier = Modifier.padding(start = 8.dp, end = 16.dp))

                RadioButton(
                    selected = !hasOtherPets,
                    onClick = { hasOtherPets = false }
                )
                Text("No", modifier = Modifier.padding(start = 8.dp))
            }

            // 如果有其他宠物，显示描述输入框
            if (hasOtherPets) {
                OutlinedTextField(
                    value = otherPetsDescription,
                    onValueChange = { otherPetsDescription = it },
                    label = { Text("Please describe your other pets") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(120.dp),
                    maxLines = 5
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Have you adopted pets before? - Radio Buttons
            Text(
                text = "Have you adopted pets before?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = hasAdoptedBefore,
                    onClick = { hasAdoptedBefore = true }
                )
                Text("Yes", modifier = Modifier.padding(start = 8.dp, end = 16.dp))

                RadioButton(
                    selected = !hasAdoptedBefore,
                    onClick = { hasAdoptedBefore = false }
                )
                Text("No", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Why do you want to adopt this pet? - Text Area
            OutlinedTextField(
                value = adoptionReason,
                onValueChange = { adoptionReason = it },
                label = { Text("Why do you want to adopt a pet?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Preferred contact time - Date Picker + Time Picker
            Text(
                text = "Preferred contact time",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 显示选择的日期
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = if (datePickerState.selectedDateMillis != null) {
                            val date = Date(datePickerState.selectedDateMillis!!)
                            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                        } else {
                            "Select Date"
                        }
                    )
                }

                // 显示选择的时间
                Button(
                    onClick = { showTimePicker = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = String.format(
                            "%02d:%02d",
                            timePickerState.hour,
                            timePickerState.minute
                        )
                    )
                }
            }

            // 日期选择器对话框
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // 时间选择器对话框
            if (showTimePicker) {
                TimePickerDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    TimePicker(state = timePickerState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Agreement Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = agreementChecked,
                    onCheckedChange = { agreementChecked = it }
                )

                Text(
                    text = "I confirm that all the information provided is accurate and I agree to follow the adoption policies.",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 保存按钮
            Button(
                onClick = onSaveProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = fullName.isNotEmpty() && email.isNotEmpty() && agreementChecked
            ) {
                Text("Save Profile")
            }

            Spacer(modifier = Modifier.height(80.dp)) // 底部空间，避免被导航栏遮挡
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 用户头像
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 提示文本
        Text(
            text = "Please fill in your profile information",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        title = { Text("Select Time") },
        text = { content() }
    )
}
