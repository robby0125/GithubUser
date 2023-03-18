package com.robby.githubuser.utils

import androidx.recyclerview.widget.DiffUtil
import com.robby.githubuser.api.model.User

class UserDiffUtilCallback(private val oldList: List<User>, private val newList: List<User>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id == newList[newItemPosition].id -> true

            oldList[oldItemPosition].login == newList[newItemPosition].login -> true

            else -> false
        }
    }
}