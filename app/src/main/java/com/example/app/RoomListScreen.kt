package com.example.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.AppTheme

data class RoomItem(
    val id: Int,
    val roomNumber: String,
    val floor: Int,
    val area: Float,
    val monthlyRent: Long,
    val status: RoomStatus,
    val hasAircon: Boolean = false,
    val hasWaterHeater: Boolean = true,
    val hasWifi: Boolean = false,
    val hasFridge: Boolean = false,
    val hasWashingMachine: Boolean = false,
    val hasPrivateBathroom: Boolean = true,
    val note: String = ""
)

enum class RoomStatus { OCCUPIED, EMPTY, MAINTENANCE }

val fakeRooms = listOf(
    RoomItem(1, "P.101", 1, 20f, 2000000, RoomStatus.OCCUPIED,
        hasAircon = true, hasWaterHeater = true, hasPrivateBathroom = true),
    RoomItem(2, "P.102", 1, 22f, 2200000, RoomStatus.EMPTY,
        hasAircon = false, hasWaterHeater = true),
    RoomItem(3, "P.103", 1, 20f, 2000000, RoomStatus.MAINTENANCE),
    RoomItem(4, "P.201", 2, 25f, 2500000, RoomStatus.OCCUPIED,
        hasAircon = true, hasWifi = true, hasFridge = true),
    RoomItem(5, "P.202", 2, 25f, 2500000, RoomStatus.EMPTY,
        hasAircon = true),
    RoomItem(6, "P.203", 2, 22f, 2200000, RoomStatus.EMPTY),
    RoomItem(7, "P.301", 3, 30f, 3000000, RoomStatus.OCCUPIED,
        hasAircon = true, hasWifi = true, hasFridge = true, hasWashingMachine = true),
    RoomItem(8, "P.302", 3, 30f, 3000000, RoomStatus.EMPTY,
        hasAircon = true),
    RoomItem(9, "P.303", 3, 28f, 2800000, RoomStatus.EMPTY)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedRoom by remember { mutableStateOf<RoomItem?>(null) }
    val groupedByFloor = fakeRooms.groupBy { it.floor }.toSortedMap()

    if (selectedRoom != null) {
        RoomDetailScreen(
            room = selectedRoom!!,
            onNavigateBack = { selectedRoom = null }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Danh sách phòng", fontWeight = FontWeight.Medium)
                       /* Text(
                            "${fakeRooms.size} phòng · " +
                                    "${fakeRooms.count { it.status == RoomStatus.OCCUPIED }} đang thuê · " +
                                    "${fakeRooms.count { it.status == RoomStatus.EMPTY }} trống",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )*/
                    }
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
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Add, contentDescription = "Thêm phòng")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Legend trạng thái
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    RoomStatusLegend(color = Color(0xFF085041), bg = Color(0xFFE1F5EE), label = "Đang thuê")
                    RoomStatusLegend(color = Color(0xFF185FA5), bg = Color(0xFFE6F1FB), label = "Phòng trống")
                    RoomStatusLegend(color = Color(0xFF633806), bg = Color(0xFFFAEEDA), label = "Bảo trì")
                }
            }

            // Nhóm theo tầng
            groupedByFloor.forEach { (floor, rooms) ->
                item {
                    Text(
                        text = "Tầng $floor",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(rooms, key = { it.id }) { room ->
                    RoomCard(
                        room = room,
                        onClick = { selectedRoom = room }
                    )
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RoomCard(room: RoomItem, onClick: () -> Unit) {
    val (statusLabel, statusBg, statusFg) = when (room.status) {
        RoomStatus.OCCUPIED    -> Triple("Đang thuê", Color(0xFFE1F5EE), Color(0xFF085041))
        RoomStatus.EMPTY       -> Triple("Phòng trống", Color(0xFFE6F1FB), Color(0xFF185FA5))
        RoomStatus.MAINTENANCE -> Triple("Bảo trì", Color(0xFFFAEEDA), Color(0xFF633806))
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Số phòng
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = statusBg,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = room.roomNumber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusFg
                )
            }

            // Thông tin
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Phòng ${room.roomNumber}",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = statusBg
                    ) {
                        Text(
                            text = statusLabel,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = statusFg
                        )
                    }
                }
                Text(
                    text = "Tầng ${room.floor} · ${room.area}m² · ${formatVndLong(room.monthlyRent)}/tháng",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Tiện nghi nhanh
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (room.hasAircon) AmenityChip("Máy lạnh")
                    if (room.hasWifi) AmenityChip("Wifi")
                    if (room.hasFridge) AmenityChip("Tủ lạnh")
                    if (room.hasWashingMachine) AmenityChip("Máy giặt")
                }
            }
        }
    }
}

@Composable
fun AmenityChip(label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun RoomStatusLegend(color: Color, bg: Color, label: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = bg) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

// ── Chi tiết 1 phòng ──
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    room: RoomItem,
    onNavigateBack: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Phòng ${room.roomNumber}", fontWeight = FontWeight.Medium)
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
                    IconButton(onClick = { isEditing = true }) {
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
            // Header card
            val (statusLabel, statusBg, statusFg) = when (room.status) {
                RoomStatus.OCCUPIED    -> Triple("Đang thuê", Color(0xFFE1F5EE), Color(0xFF085041))
                RoomStatus.EMPTY       -> Triple("Phòng trống", Color(0xFFE6F1FB), Color(0xFF185FA5))
                RoomStatus.MAINTENANCE -> Triple("Bảo trì", Color(0xFFFAEEDA), Color(0xFF633806))
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = statusBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Phòng ${room.roomNumber}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusFg
                        )
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = statusFg.copy(alpha = 0.8f)
                        )
                    }
                    Text(
                        text = formatVndLong(room.monthlyRent),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusFg
                    )
                }
            }

            // Thông tin phòng
            DetailSection(title = "Thông tin phòng") {
                DetailRow(label = "Số phòng", value = room.roomNumber)
                DetailRow(label = "Tầng", value = "Tầng ${room.floor}")
                DetailRow(label = "Diện tích", value = "${room.area} m²")
                DetailRow(label = "Giá thuê", value = "${formatVndLong(room.monthlyRent)}/tháng")
                DetailRow(label = "Trạng thái", value = statusLabel, valueColor = statusFg)
            }

            // Tiện nghi riêng phòng
            DetailSection(title = "Tiện nghi trong phòng") {
                AmenityRow(label = "Máy lạnh / Điều hòa", hasIt = room.hasAircon)
                AmenityRow(label = "Máy nước nóng", hasIt = room.hasWaterHeater)
                AmenityRow(label = "Wifi riêng", hasIt = room.hasWifi)
                AmenityRow(label = "Tủ lạnh", hasIt = room.hasFridge)
                AmenityRow(label = "Máy giặt", hasIt = room.hasWashingMachine)
                AmenityRow(label = "WC riêng", hasIt = room.hasPrivateBathroom)
            }

            if (room.note.isNotBlank()) {
                DetailSection(title = "Ghi chú") {
                    Text(
                        text = room.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoomList() {
    AppTheme {
        RoomListScreen()
    }
}