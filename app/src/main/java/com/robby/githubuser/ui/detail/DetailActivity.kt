package com.robby.githubuser.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.robby.githubuser.api.model.UserDetail
import com.robby.githubuser.databinding.ActivityDetailBinding
import com.robby.githubuser.enums.StatusResponse
import com.robby.githubuser.ui.adapter.UserFollowPagerAdapter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "User Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.detailContent.visibility = View.INVISIBLE
        binding.layoutError.root.visibility = View.INVISIBLE

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            setupTabs(username)

            with(detailViewModel) {
                getUserDetail(username)

                detail.observe(this@DetailActivity) { response ->
                    when (response.status) {
                        StatusResponse.SUCCESS -> populateUserDetail(response.body!!)
                        StatusResponse.EMPTY -> showFailed(response.message!!)
                        StatusResponse.ERROR -> showFailed(response.message!!)
                    }
                }

                isLoading.observe(this@DetailActivity) { isLoading ->
                    showLoading(isLoading)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupTabs(username: String) {
        val pageTitles = listOf("Follower", "Following")
        val pagerAdapter = UserFollowPagerAdapter(this)
        pagerAdapter.username = username

        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = pageTitles[position]
        }.attach()
    }

    private fun populateUserDetail(userDetail: UserDetail) {
        with(binding) {
            detailContent.visibility = View.VISIBLE

            val textFollower = "${userDetail.followers} Follower"
            val textFollowing = "${userDetail.following} Following"

            tvFullName.text = userDetail.name
            tvUsername.text = userDetail.login
            tvFollower.text = textFollower
            tvFollowing.text = textFollowing

            Glide.with(this@DetailActivity)
                .load(userDetail.avatarUrl)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imgUserPicture)
        }
    }

    private fun showFailed(message: String) {
        with(binding.layoutError) {
            root.visibility = View.VISIBLE
            tvErrorMessage.text = message
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}