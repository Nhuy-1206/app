package com.example.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillScreen(
    tenant: TenantUi = fakeTenants[0],
    onNavigateBack: () -> Unit = {}
) {
    var electricOld by remember { mutableStateOf("100") }
    var electricNew by remember { mutableStateOf("") }
    var electricPrice by remember { mutableStateOf("3500") }
    var waterOld by remember { mutableStateOf("10") }
    var waterNew by remember { mutableStateOf("") }
    var waterPrice by remember { mutableStateOf("15000") }
    var otherFee by remember { mutableStateOf("0") }
    var month by remember { mutableStateOf("Tháng 4/2025") }

    val electricUsed = (electricNew.toLongOrNull() ?: 0L) - (electricOld.toLongOrNull() ?: 0L)
    val electricCost = electricUsed * (electricPrice.toLongOrNull() ?: 0L)
    val waterUsed = (waterNew.toLongOrNull() ?: 0L) - (waterOld.toLongOrNull() ?: 0L)
    val waterCost = waterUsed * (waterPrice.toLongOrNull() ?: 0L)
    val rentCost = tenant.monthlyRent.replace(".", "").replace("đ", "").toLongOrNull() ?: 0L
    val otherCost = otherFee.toLongOrNull() ?: 0L
    val total = rentCost + electricCost + waterCost + otherCost
    val isValid = electricNew.isNotBlank() && waterNew.isNotBlank()
            && electricUsed >= 0 && waterUsed >= 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tính tiền tháng", fontWeight = FontWeight.Medium)
                        Text(tenant.fullName, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
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
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            RentalTextField(value = month, onValueChange = { month = it },
                label = "Kỳ thanh toán", placeholder = "Tháng 4/2025")

            SectionLabel(title = "Tiền phòng")
            BillSummaryRow(label = "Tiền thuê phòng", value = formatVndLong(rentCost))

            SectionLabel(title = "Tiền điện")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RentalTextField(value = electricOld, onValueChange = { electricOld = it },
                    label = "Chỉ số cũ", placeholder = "100",
                    keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                RentalTextField(value = electricNew, onValueChange = { electricNew = it },
                    label = "Chỉ số mới", placeholder = "150",
                    keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
            }
            RentalTextField(value = electricPrice, onValueChange = { electricPrice = it },
                label = "Đơn giá (đ/kWh)", placeholder = "3500",
                keyboardType = KeyboardType.Number)
            if (electricNew.isNotBlank()) {
                BillSummaryRow(
                    label = "Điện dùng: ${electricUsed}kWh × ${electricPrice}đ",
                    value = formatVndLong(electricCost),
                    valueColor = if (electricUsed < 0) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            SectionLabel(title = "Tiền nước")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RentalTextField(value = waterOld, onValueChange = { waterOld = it },
                    label = "Chỉ số cũ", placeholder = "10",
                    keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                RentalTextField(value = waterNew, onValueChange = { waterNew = it },
                    label = "Chỉ số mới", placeholder = "15",
                    keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
            }
            RentalTextField(value = waterPrice, onValueChange = { waterPrice = it },
                label = "Đơn giá (đ/m³)", placeholder = "15000",
                keyboardType = KeyboardType.Number)
            if (waterNew.isNotBlank()) {
                BillSummaryRow(
                    label = "Nước dùng: ${waterUsed}m³ × ${waterPrice}đ",
                    value = formatVndLong(waterCost),
                    valueColor = if (waterUsed < 0) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            SectionLabel(title = "Phí khác")
            RentalTextField(value = otherFee, onValueChange = { otherFee = it },
                label = "Phí khác (internet, rác...)", placeholder = "0",
                keyboardType = KeyboardType.Number)

            HorizontalDivider(thickness = 1.dp)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tổng cộng", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(formatVndLong(total), fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                }
            }

            Button(
                onClick = { onNavigateBack() },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lưu hóa đơn", fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun BillSummaryRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = if (valueColor == Color.Unspecified)
                MaterialTheme.colorScheme.onSurface else valueColor)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBill() {
    AppTheme { BillScreen() }
}