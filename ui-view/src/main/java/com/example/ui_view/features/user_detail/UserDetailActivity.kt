package com.example.ui_view.features.user_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.data.model.GithubUserDetail
import com.example.feature_user.UserDetailState
import com.example.feature_user.UserDetailViewModel
import com.example.ui_view.R
import com.example.ui_view.databinding.ActivityUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
            }

            binding.appBar.updateLayoutParams {
                height = (resources.displayMetrics.density * 320).toInt() + systemBars.top
            }
            insets
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userDetailState.collectLatest {
                    when (it) {
                        is UserDetailState.Error -> TODO()
                        UserDetailState.Loading -> Unit//TODO
                        is UserDetailState.Success -> setupView(it.data)
                    }
                }
            }
        }
    }

    private fun setupView(detail: GithubUserDetail) {
        binding.statFollowers.apply {
            tvStatCount.text = detail.followers.toString()
            tvStatLabel.text = "Followers"
        }

        binding.statFollowing.apply {
            tvStatCount.text = detail.following.toString()
            tvStatLabel.text = "Following"
        }

        binding.statRepos.apply {
            tvStatCount.text = detail.public_repos.toString()
            tvStatLabel.text = "Repos"
        }

        binding.infoLocation.apply {
            tvInfoText.text = detail.location ?: "Not Available"
            ivInfoIcon.setImageResource(R.drawable.ic_location)
        }

        binding.infoBlog.tvInfoText.text = detail.blog

        Glide.with(this).load(detail.avatar_url).into(binding.ivAvatarLarge)
    }

    companion object {
        fun createIntent(context: Context, userName: String): Intent =
            Intent(context, UserDetailActivity::class.java).apply {
                putExtra(UserDetailViewModel.USER_NAME, userName)
            }
    }
}