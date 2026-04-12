package com.github.studyandroid.app.ui.user.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.studyandroid.app.domain.model.User
import com.github.studyandroid.app.ui.theme.AppTheme

@Composable
fun UserDetailScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 收集一次性 Effect：导航事件由 ViewModel 驱动，Screen 只响应
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UserDetailEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    UserDetailContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDetailContent(
    uiState: UserDetailUiState,
    onIntent: (UserDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("用户详情", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onIntent(UserDetailIntent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
            uiState.user != null -> {
                val user = uiState.user
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = user.avatarUrl,
                            contentDescription = "${user.name}的头像",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                        )
                    }
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "@${user.username}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.size(24.dp))
                    UserDetailRow(label = "邮箱", value = user.email)
                    UserDetailRow(label = "电话", value = user.phone)
                    UserDetailRow(label = "网站", value = user.website)
                    UserDetailRow(label = "公司", value = user.company)
                    UserDetailRow(label = "口号", value = user.companySlogan)
                    UserDetailRow(label = "业务", value = user.companyBs)
                }
            }
        }
    }
}

@Composable
private fun UserDetailRow(label: String, value: String) {
    ListItem(
        // 标签在上（overlineContent），值在主行（headlineContent）
        // 符合用户认知：先看标签了解语义，再看值获取内容
        overlineContent = { Text(label, style = MaterialTheme.typography.labelSmall) },
        headlineContent = { Text(value) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun UserDetailPreview() {
    AppTheme {
        UserDetailContent(
            uiState = UserDetailUiState(
                user = User(
                    id = 1,
                    name = "张三",
                    username = "zhangsan",
                    email = "zhangsan@example.com",
                    phone = "123-456-7890",
                    website = "example.com",
                    company = "美团科技",
                    companySlogan = "让生活更美好",
                    companyBs = "本地生活服务平台",
                    avatarUrl = "http://localhost:8080/avatars/1.jpg"
                )
            ),
            onIntent = {}
        )
    }
}
