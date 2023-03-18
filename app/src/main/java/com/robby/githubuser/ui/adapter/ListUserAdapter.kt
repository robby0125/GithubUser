package com.robby.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.robby.githubuser.api.model.User
import com.robby.githubuser.databinding.ItemUserBinding
import com.robby.githubuser.utils.UserDiffUtilCallback

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private val listUser = ArrayList<User>()
    private var onClick: OnItemClickListener? = null

    fun setData(listUser: List<User>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffUtilCallback(this.listUser, listUser))
        this.listUser.clear()
        this.listUser.addAll(listUser)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickListener(onClick: OnItemClickListener) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserAdapter.ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUserAdapter.ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvUsername.text = user.login

                val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(16))
                Glide.with(this.root.context)
                    .load(user.avatarUrl)
                    .apply(requestOptions)
                    .into(imgUserPicture)

                root.setOnClickListener { onClick?.onUserItemClick(user) }
            }
        }
    }

    interface OnItemClickListener {
        fun onUserItemClick(user: User)
    }
}