package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


data class TenantUi(
    val id: Int,
    val fullName: String,
    val phone: String,
    val cccd: String,
    val moveInDate: String,
    val contractEndDate: String,
    val monthlyRent: String,
    val deposit: String,
    val status: TenantStatus
)

enum class TenantStatus { ACTIVE, EXPIRING_SOON, ENDED }

val fakeTenants = listOf(
    TenantUi(
        id = 1,
        fullName = "Nguyễn Văn Tài",
        phone = "0901 234 567",
        cccd = "079203012345",
        moveInDate = "01/03/2024",
        contractEndDate = "01/03/2025",
        monthlyRent = "2.500.000đ",
        deposit = "3.000.000đ",
        status = TenantStatus.ACTIVE
    ),
    TenantUi(
        id = 2,
        fullName = "Trần Thị Lan",
        phone = "0912 345 678",
        cccd = "079203098765",
        moveInDate = "01/06/2024",
        contractEndDate = "19/05/2025",
        monthlyRent = "2.500.000đ",
        deposit = "3.000.000đ",
        status = TenantStatus.EXPIRING_SOON
    ),
    TenantUi(
        id = 3,
        fullName = "Phạm Minh Hùng",
        phone = "0933 456 789",
        cccd = "079203011111",
        moveInDate = "01/01/2023",
        contractEndDate = "01/01/2024",
        monthlyRent = "2.000.000đ",
        deposit = "2.000.000đ",
        status = TenantStatus.ENDED
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToTenants = {
                                navController.navigate("tenant_list")
                            }
                        )
                    }
                    composable("tenant_list") {
                        TenantListScreen(
                            onNavigateToAdd = {
                                navController.navigate("tenant_add")
                            },
                            onNavigateToDetail = { id ->
                                navController.navigate("tenant_detail/$id")
                            }
                        )
                    }
                    composable("tenant_add") {
                        AddTenantScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("tenant_detail/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
                        val tenant = fakeTenants.find { it.id == id } ?: fakeTenants[0]
                        TenantDetailScreen(
                            tenant = tenant,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToBill = { tenantId ->
                                navController.navigate("bill/$tenantId")
                            }
                        )
                    }
                    composable("bill/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
                        val tenant = fakeTenants.find { it.id == id } ?: fakeTenants[0]
                        BillScreen(
                            tenant = tenant,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AvatarCircle(name: String) {
            val initials = name.trim().split(" ")
                .takeLast(2)
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .joinToString("")

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = initials,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }


@Composable
fun StatusBadge(status: TenantStatus) {
            val (label, bg, fg) = when (status) {
                TenantStatus.ACTIVE ->
                    Triple("Đang thuê", Color(0xFFE1F5EE), Color(0xFF085041))
                TenantStatus.EXPIRING_SOON ->
                    Triple("Sắp hết hạn", Color(0xFFFAEEDA), Color(0xFF633806))
                TenantStatus.ENDED ->
                    Triple("Đã trả phòng", Color(0xFFF1EFE8), Color(0xFF5F5E5A))
            }
            Surface(shape = RoundedCornerShape(20.dp), color = bg) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = fg
                )
            }
        }

@Composable
fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
            Column(modifier = modifier) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

@Composable
fun TenantCard(tenant: TenantUi,
               onCardClick: () -> Unit = {},
               onBillClick: () -> Unit = {} ) {
    Card(
        onClick = onCardClick,
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
        Column(modifier = Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarCircle(name = tenant.fullName)
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = tenant.fullName,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        StatusBadge(status = tenant.status)
                    }
                    Text(
                        text = "${tenant.phone} · CCCD ${tenant.cccd}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(thickness = 0.5.dp)
            Spacer(Modifier.height(10.dp))

            // Thông tin 2 cột
            Row {
                InfoItem(
                    label = "Ngày vào",
                    value = tenant.moveInDate,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Tiền cọc",
                    value = tenant.deposit,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(6.dp))
            Row {
                InfoItem(
                    label = "HĐ đến",
                    value = tenant.contractEndDate,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Tiền phòng",
                    value = "${tenant.monthlyRent}/tháng",
                    modifier = Modifier.weight(1f)
                )
            }

            // Buttons
            if (tenant.status != TenantStatus.ENDED) {
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Gọi điện",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                   /* Button(
                        onClick = onBillClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Tính tiền tháng",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }     bỏ nút tính tiền trên thẻ khách thuê*/
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantListScreen(onNavigateToAdd: () -> Unit = {},
                     onNavigateToDetail: (Int) -> Unit = {}) {
            val grouped = fakeTenants.groupBy { it.status }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Khách thuê",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Phòng trọ của bạn",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = onNavigateToAdd) {
                                Icon(Icons.Default.Add, contentDescription = "Thêm")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        TenantStatus.ACTIVE        to "Đang thuê",
                        TenantStatus.EXPIRING_SOON to "Sắp hết hạn hợp đồng",
                        TenantStatus.ENDED         to "Đã kết thúc"
                    ).forEach { (status, label) ->
                        val group = grouped[status] ?: emptyList()
                        if (group.isNotEmpty()) {
                            item {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )
                            }
                            items(group, key = { it.id }) { tenant ->
                                TenantCard(tenant = tenant,
                                    onCardClick = { onNavigateToDetail(tenant.id) },
                                onBillClick = { onNavigateToDetail(tenant.id) }
                                )
                            }
                        }
                    }
                }
            }
        }




@Preview(showBackground = true)
@Composable
fun PreviewTenantList() {
    AppTheme {
        TenantListScreen()
    }
}