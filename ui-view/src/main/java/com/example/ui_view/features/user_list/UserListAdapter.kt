package com.example.ui_view.features.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.model.GithubUser
import com.example.ui_view.databinding.ItemUserBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserListAdapter @Inject constructor() :
    PagingDataAdapter<GithubUser, UserListAdapter.UserViewHolder>(UserDiffCallback) {
    var onItemClick: (GithubUser) -> Unit = {}

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { user ->
            holder.bind(user)
            holder.itemView.setOnClickListener {
                onItemClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            binding.tvLogin.text = user.login
            binding.tvType.text = user.type
            Glide.with(binding.imageView.context).load(user.avatar_url).circleCrop()
                .into(binding.imageView)
        }
    }


    object UserDiffCallback : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(
            oldItem: GithubUser, newItem: GithubUser
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: GithubUser, newItem: GithubUser
        ): Boolean = oldItem == newItem
    }
}