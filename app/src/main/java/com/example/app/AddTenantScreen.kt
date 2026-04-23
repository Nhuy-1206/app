package com.example.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantScreen(
    onNavigateBack: () -> Unit = {}
) {
    // State cho từng field
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cccd by remember { mutableStateOf("") }
    var hometown by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("") }
    var contractMonths by remember { mutableStateOf("12") }
    var monthlyRent by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    // Validate
    val isFormValid = fullName.isNotBlank()
            && phone.isNotBlank()
            && cccd.isNotBlank()
            && moveInDate.isNotBlank()
            && monthlyRent.isNotBlank()
            && deposit.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm khách thuê", fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Section 1: Thông tin cá nhân ──
            SectionLabel(title = "Thông tin cá nhân")

            RentalTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Họ và tên *",
                placeholder = "Nguyễn Văn A"
            )

            RentalTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Số điện thoại *",
                placeholder = "09xx xxx xxx",
                keyboardType = KeyboardType.Phone
            )

            RentalTextField(
                value = cccd,
                onValueChange = { if (it.length <= 12) cccd = it },
                label = "Số CCCD *",
                placeholder = "079xxxxxxxxx",
                keyboardType = KeyboardType.Number,
                supportingText = "${cccd.length}/12 số"
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RentalTextField(
                    value = hometown,
                    onValueChange = { hometown = it },
                    label = "Quê quán",
                    placeholder = "Bình Định",
                    modifier = Modifier.weight(1f)
                )
                RentalTextField(
                    value = occupation,
                    onValueChange = { occupation = it },
                    label = "Nghề nghiệp",
                    placeholder = "Sinh viên",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(4.dp))

            // ── Section 2: Thông tin hợp đồng ──
            SectionLabel(title = "Thông tin hợp đồng")

            RentalTextField(
                value = moveInDate,
                onValueChange = { moveInDate = it },
                label = "Ngày vào ở *",
                placeholder = "dd/mm/yyyy",
                keyboardType = KeyboardType.Number,
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RentalTextField(
                    value = monthlyRent,
                    onValueChange = { monthlyRent = it },
                    label = "Tiền phòng/tháng *",
                    placeholder = "2500000",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                RentalTextField(
                    value = deposit,
                    onValueChange = { deposit = it },
                    label = "Tiền cọc *",
                    placeholder = "3000000",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            // Thời hạn hợp đồng — dùng dropdown
            ContractMonthsDropdown(
                selected = contractMonths,
                onSelected = { contractMonths = it }
            )

            RentalTextField(
                value = note,
                onValueChange = { note = it },
                label = "Ghi chú",
                placeholder = "Ghi chú thêm nếu có...",
                singleLine = false,
                minLines = 3
            )

            Spacer(Modifier.height(8.dp))

            // Nút lưu
            Button(
                onClick = { onNavigateBack() },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Lưu khách thuê",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            // Nút hủy
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Hủy",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Reusable components ──

@Composable
fun SectionLabel(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
    HorizontalDivider(thickness = 0.5.dp)
    Spacer(Modifier.height(4.dp))
}

@Composable
fun RentalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    supportingText: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        placeholder = { Text(placeholder, style = MaterialTheme.typography.bodySmall) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = minLines,
        trailingIcon = trailingIcon,
        supportingText = if (supportingText != null) {
            { Text(supportingText, style = MaterialTheme.typography.labelSmall) }
        } else null,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractMonthsDropdown(
    selected: String,
    onSelected: (String) -> Unit
) {
    val options = listOf("3", "6", "12", "24")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = "$selected tháng",
            onValueChange = {},
            readOnly = true,
            label = { Text("Thời hạn hợp đồng", style = MaterialTheme.typography.bodySmall) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text("$option tháng") },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddTenant() {
    AppTheme {
        AddTenantScreen()
    }
}