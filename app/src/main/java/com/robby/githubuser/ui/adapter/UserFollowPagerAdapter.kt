package com.robby.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.robby.githubuser.ui.detail.UserFollowFragment

class UserFollowPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username = ""

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = UserFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(UserFollowFragment.ARG_POSITION, position)
            putString(UserFollowFragment.ARG_USERNAME, username)
        }

        return fragment
    }
}