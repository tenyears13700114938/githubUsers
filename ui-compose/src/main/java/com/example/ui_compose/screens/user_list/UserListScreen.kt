package com.example.ui_compose.screens.user_list

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.feature_user.UserListViewModel
import com.example.ui_compose.components.UserListContent

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    onUserClick: (String) -> Unit
) {
    val pagingItems = viewModel.userListFlow.collectAsLazyPagingItems()

    UserListContent(
        userPagingItems = pagingItems,
        onUserClick = onUserClick
    )
}