package com.github.studyandroid.app.ui.user.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.studyandroid.app.domain.model.User
import com.github.studyandroid.app.ui.theme.AppTheme

@Composable
fun UserListScreen(
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UserListEffect.NavigateToDetail -> onNavigateToDetail(effect.userId)
                is UserListEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    UserListContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListContent(
    uiState: UserListUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (UserListIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("用户列表", fontWeight = FontWeight.Bold) },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { onIntent(UserListIntent.Refresh) }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "刷新")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { onIntent(UserListIntent.Refresh) },
            modifier = Modifier.padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.users.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = WindowInsets.navigationBars.asPaddingValues() // 预留系统导航栏/手势区高度，防止最后一项被遮挡
                    ) {
                        items(uiState.users, key = { it.id }) { user ->
                            UserItem(user = user) {
                                onIntent(UserListIntent.UserClicked(user.id))
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserItem(user: User, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(user.name, fontWeight = FontWeight.Medium) },
        supportingContent = {
            Column {
                Text(user.email, style = MaterialTheme.typography.bodySmall)
                Text(user.company, style = MaterialTheme.typography.bodySmall)
            }
        },
        leadingContent = {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "${user.name}的头像",
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        },
        trailingContent = {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "查看详情")
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
private fun UserListPreview() {
    AppTheme {
        UserListContent(
            uiState = UserListUiState(
                users = List(5) {
                    User(
                        id = it,
                        name = "用户 $it",
                        username = "user$it",
                        email = "user$it@example.com",
                        phone = "123-456-7890",
                        website = "example.com",
                        company = "公司 $it",
                        companySlogan = "口号 $it",
                        companyBs = "业务 $it",
                        avatarUrl = "http://localhost:8080/avatars/1.jpg"
                    )
                }
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {}
        )
    }
}
