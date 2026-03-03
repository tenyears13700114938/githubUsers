package com.example.ui_compose.screens.user_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.data.model.GithubUserDetail
import com.example.feature_user.UserDetailState
import com.example.feature_user.UserDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val userDetail by viewModel.userDetailState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff6200EE)
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (userDetail) {
            is UserDetailState.Error -> Unit//TODO()
            UserDetailState.Loading -> Unit//TODO()
            is UserDetailState.Success -> UserDetailContent(
                innerPadding,
                (userDetail as UserDetailState.Success).data
            )
        }
    }
}

@Composable
fun UserDetailContent(innerPadding: PaddingValues, userDetail: GithubUserDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xff6200EE)
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. 用户大头像
            AsyncImage(
                model = userDetail.avatar_url,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. 登录 ID
            Text(
                text = userDetail.login,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = userDetail.type,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // 3. 数据统计卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetailStatItem("Followers", userDetail.followers.toString())
                DetailStatItem("Following", userDetail.following.toString())
                DetailStatItem("Repos", userDetail.public_repos.toString())
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        InfoItem(userDetail.location)
        InfoItem(userDetail.blog)
    }
}

@Composable
fun DetailStatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun InfoItem(info: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .height(20.dp)
                .width(20.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = ""
        )
        Text(text = if (info.isNullOrEmpty()) "Not Available" else info, fontSize = 13.sp)
    }
}