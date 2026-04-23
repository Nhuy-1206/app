package com.example.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.AppTheme

data class BoardingHouse(
    val name: String = "Nhà trọ Hoàng Gia",
    val address: String = "123 Đường Lê Lợi, P.Bến Nghé, Q.1, TP.HCM",
    val totalFloors: Int = 3,
    val totalRooms: Int = 9,
    val ownerName: String = "Nguyễn Văn A",
    val ownerPhone: String = "0901 234 567",
    val hasParking: Boolean = true,
    val hasSecurity: Boolean = true,
    val hasCamera: Boolean = true,
    val hasElevator: Boolean = false,
    val hasGenerator: Boolean = false,
    val hasGarden: Boolean = false,
    val note: String = "Nhà trọ yên tĩnh, an ninh tốt, gần trường đại học."
)

val fakeBoardingHouse = BoardingHouse()

@Composable
fun BoardingHouseScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToRooms: () -> Unit = {}
) {
    var house by remember { mutableStateOf(fakeBoardingHouse) }
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        BoardingHouseEditScreen(
            house = house,
            onSave = { updated ->
                house = updated
                isEditing = false
            },
            onCancel = { isEditing = false }
        )
    } else {
        BoardingHouseViewScreen(
            house = house,
            onNavigateBack = onNavigateBack,
            onEdit = { isEditing = true },
            onNavigateToRooms = onNavigateToRooms
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardingHouseViewScreen(
    house: BoardingHouse,
    onNavigateBack: () -> Unit,
    onEdit: () -> Unit,
    onNavigateToRooms: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nhà trọ của tôi", fontWeight = FontWeight.Medium)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Chỉnh sửa")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Header ──
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = house.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = house.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        QuickStat(label = "Tổng tầng", value = "${house.totalFloors}")
                        QuickStat(label = "Tổng phòng", value = "${house.totalRooms}")
                        QuickStat(label = "Đang thuê", value = "2")
                        QuickStat(label = "Trống", value = "${house.totalRooms - 2}")
                    }
                }
            }

            // ── Thông tin chủ trọ ──
            DetailSection(title = "Thông tin chủ trọ") {
                DetailRow(label = "Họ tên", value = house.ownerName)
                DetailRow(
                    label = "Số điện thoại",
                    value = house.ownerPhone,
                    valueColor = MaterialTheme.colorScheme.primary
                )
            }

            // ── Thông tin tổng thể ──
            DetailSection(title = "Thông tin nhà trọ") {
                DetailRow(label = "Tên nhà trọ", value = house.name)
                DetailRow(label = "Địa chỉ", value = house.address)
                DetailRow(label = "Số tầng", value = "${house.totalFloors} tầng")
                DetailRow(label = "Tổng số phòng", value = "${house.totalRooms} phòng")
            }

            // ── Tiện ích chung ──
            DetailSection(title = "Tiện ích chung của nhà trọ") {
                AmenityRow(label = "Bãi giữ xe", hasIt = house.hasParking)
                AmenityRow(label = "Bảo vệ 24/7", hasIt = house.hasSecurity)
                AmenityRow(label = "Camera an ninh", hasIt = house.hasCamera)
                AmenityRow(label = "Thang máy", hasIt = house.hasElevator)
                AmenityRow(label = "Máy phát điện", hasIt = house.hasGenerator)
                AmenityRow(label = "Sân vườn", hasIt = house.hasGarden)
            }

            // ── Ghi chú ──
            if (house.note.isNotBlank()) {
                DetailSection(title = "Ghi chú") {
                    Text(
                        text = house.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // ── Nút xem danh sách phòng ──
            Button(
                onClick = onNavigateToRooms,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Xem danh sách phòng", fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardingHouseEditScreen(
    house: BoardingHouse,
    onSave: (BoardingHouse) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(house.name) }
    var address by remember { mutableStateOf(house.address) }
    var totalFloors by remember { mutableStateOf(house.totalFloors.toString()) }
    var totalRooms by remember { mutableStateOf(house.totalRooms.toString()) }
    var ownerName by remember { mutableStateOf(house.ownerName) }
    var ownerPhone by remember { mutableStateOf(house.ownerPhone) }
    var hasParking by remember { mutableStateOf(house.hasParking) }
    var hasSecurity by remember { mutableStateOf(house.hasSecurity) }
    var hasCamera by remember { mutableStateOf(house.hasCamera) }
    var hasElevator by remember { mutableStateOf(house.hasElevator) }
    var hasGenerator by remember { mutableStateOf(house.hasGenerator) }
    var hasGarden by remember { mutableStateOf(house.hasGarden) }
    var note by remember { mutableStateOf(house.note) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chỉnh sửa nhà trọ", fontWeight = FontWeight.Medium)
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Hủy"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionLabel(title = "Thông tin nhà trọ")

            RentalTextField(
                value = name,
                onValueChange = { name = it },
                label = "Tên nhà trọ",
                placeholder = "Nhà trọ Hoàng Gia"
            )
            RentalTextField(
                value = address,
                onValueChange = { address = it },
                label = "Địa chỉ",
                placeholder = "123 Đường ABC...",
                singleLine = false,
                minLines = 2
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RentalTextField(
                    value = totalFloors,
                    onValueChange = { totalFloors = it },
                    label = "Số tầng",
                    placeholder = "3",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                RentalTextField(
                    value = totalRooms,
                    onValueChange = { totalRooms = it },
                    label = "Tổng phòng",
                    placeholder = "9",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionLabel(title = "Thông tin chủ trọ")
            RentalTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                label = "Họ và tên chủ trọ",
                placeholder = "Nguyễn Văn A"
            )
            RentalTextField(
                value = ownerPhone,
                onValueChange = { ownerPhone = it },
                label = "Số điện thoại chủ trọ",
                placeholder = "09xx xxx xxx",
                keyboardType = KeyboardType.Phone
            )

            SectionLabel(title = "Tiện ích chung")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    0.5.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    AmenityToggle("Bãi giữ xe", hasParking) { hasParking = it }
                    AmenityToggle("Bảo vệ 24/7", hasSecurity) { hasSecurity = it }
                    AmenityToggle("Camera an ninh", hasCamera) { hasCamera = it }
                    AmenityToggle("Thang máy", hasElevator) { hasElevator = it }
                    AmenityToggle("Máy phát điện", hasGenerator) { hasGenerator = it }
                    AmenityToggle("Sân vườn", hasGarden) { hasGarden = it }
                }
            }

            SectionLabel(title = "Ghi chú")
            RentalTextField(
                value = note,
                onValueChange = { note = it },
                label = "Ghi chú",
                placeholder = "Mô tả thêm về nhà trọ...",
                singleLine = false,
                minLines = 3
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    onSave(
                        house.copy(
                            name = name,
                            address = address,
                            totalFloors = totalFloors.toIntOrNull() ?: house.totalFloors,
                            totalRooms = totalRooms.toIntOrNull() ?: house.totalRooms,
                            ownerName = ownerName,
                            ownerPhone = ownerPhone,
                            hasParking = hasParking,
                            hasSecurity = hasSecurity,
                            hasCamera = hasCamera,
                            hasElevator = hasElevator,
                            hasGenerator = hasGenerator,
                            hasGarden = hasGarden,
                            note = note
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lưu thông tin", fontWeight = FontWeight.Medium)
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Hủy")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoardingHouse() {
    AppTheme {
        BoardingHouseScreen()
    }
}