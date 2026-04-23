package com.example.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailScreen(
    tenant: TenantUi = fakeTenants[0],
    onNavigateBack: () -> Unit = {},
    onNavigateToBill: (Int) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Thông tin", "Hợp đồng", "Lịch sử")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tenant.fullName, fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
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
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            ProfileHeader(tenant = tenant)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            when (selectedTab) {
                0 -> InfoTab(tenant = tenant)
                1 -> ContractTab(tenant = tenant, onNavigateToBill = onNavigateToBill)
                2 -> HistoryTab()
            }
        }
    }
}

@Composable
fun ProfileHeader(tenant: TenantUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val initials = tenant.fullName.trim().split(" ")
                .takeLast(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(60.dp).clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Text(text = initials, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium, color = Color.White)
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = tenant.fullName, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium, color = Color.White)
                Text(text = tenant.phone, style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f))
                StatusBadge(status = tenant.status)
            }
        }
    }
}

@Composable
fun InfoTab(tenant: TenantUi) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DetailSection(title = "Thông tin cá nhân") {
            DetailRow("Họ và tên", tenant.fullName)
            DetailRow("Số điện thoại", tenant.phone,
                valueColor = MaterialTheme.colorScheme.primary)
            DetailRow("Số CCCD", tenant.cccd)
        }
        DetailSection(title = "Thông tin phòng") {
            DetailRow("Ngày vào ở", tenant.moveInDate)
            DetailRow("Tiền phòng", "${tenant.monthlyRent}/tháng")
            DetailRow("Tiền cọc", tenant.deposit)
        }
    }
}

@Composable
fun ContractTab(tenant: TenantUi, onNavigateToBill: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DetailSection(title = "Hợp đồng") {
            DetailRow("Ngày bắt đầu", tenant.moveInDate)
            DetailRow("Ngày kết thúc", tenant.contractEndDate)
            DetailRow("Trạng thái", when (tenant.status) {
                TenantStatus.ACTIVE -> "Đang hiệu lực"
                TenantStatus.EXPIRING_SOON -> "Sắp hết hạn"
                TenantStatus.ENDED -> "Đã hết hạn"
            })
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tiến trình hợp đồng", style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.fillMaxWidth().height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(tenant.moveInDate, style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("75% đã qua", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                    Text(tenant.contractEndDate, style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Button(
            onClick = { onNavigateToBill(tenant.id) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Tính tiền tháng này", fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun HistoryTab() {
    val fakeHistory = listOf(
        Triple("Tháng 4/2025", "4.250.000đ", "Đã thanh toán"),
        Triple("Tháng 3/2025", "4.100.000đ", "Đã thanh toán"),
        Triple("Tháng 2/2025", "4.300.000đ", "Đã thanh toán"),
        Triple("Tháng 1/2025", "4.050.000đ", "Đã thanh toán"),
    )
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fakeHistory.forEach { (month, amount, status) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    0.5.dp, MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(month, style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium)
                        Text(status, style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF085041))
                    }
                    Text(amount, style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTenantDetail() {
    AppTheme { TenantDetailScreen() }
}