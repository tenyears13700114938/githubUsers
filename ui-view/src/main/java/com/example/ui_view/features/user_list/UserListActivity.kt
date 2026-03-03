package com.example.ui_view.features.user_list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feature_user.UserListViewModel
import com.example.ui_view.R
import com.example.ui_view.databinding.ActivityUserListBinding
import com.example.ui_view.features.user_detail.UserDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding

    private val viewModel: UserListViewModel by viewModels()

    @Inject
    lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.shimmerContainer.post {
            val screenHeight = binding.shimmerView.height
            val itemHeight = (resources.displayMetrics.density * 68).toInt()
            val count = screenHeight / itemHeight
            setupShimmer(count)
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        userListAdapter.onItemClick = {
            startActivity(UserDetailActivity.createIntent(this, it.login))
        }
        val lm = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.apply {
            layoutManager = lm
            adapter = userListAdapter
//            addItemDecoration(
//                DividerItemDecoration(this@UserListActivity, DividerItemDecoration.VERTICAL)
//            )
        }

        userListAdapter.addLoadStateListener { loadStates ->
            val isInitialLoading = loadStates.source.refresh is LoadState.Loading

            binding.shimmerView.isVisible = isInitialLoading
            if (isInitialLoading) {
                binding.shimmerView.startShimmer()
            } else {
                binding.shimmerView.stopShimmer()
            }

            binding.recyclerView.isVisible = !isInitialLoading
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userListFlow.collectLatest { pagingData ->
                    userListAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setupShimmer(count: Int) {
        repeat(count) {
            val itemView =
                layoutInflater.inflate(R.layout.item_user_shimmer, binding.shimmerContainer, false)
            binding.shimmerContainer.addView(itemView)
        }
    }
}