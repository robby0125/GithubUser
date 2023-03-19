package com.robby.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.robby.githubuser.R
import com.robby.githubuser.api.model.User
import com.robby.githubuser.databinding.ActivityMainBinding
import com.robby.githubuser.enums.StatusResponse
import com.robby.githubuser.ui.adapter.ListUserAdapter
import com.robby.githubuser.ui.detail.DetailActivity
import com.robby.githubuser.utils.MarginItemDecoration

class MainActivity : AppCompatActivity(), ListUserAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()
    private val listUserAdapter = ListUserAdapter()

    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutError.root.visibility = View.INVISIBLE
        binding.rvListUsers.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            val verticalMargin = resources.getDimensionPixelSize(R.dimen.vertical_margin)
            val horizontalMargin = resources.getDimensionPixelSize(R.dimen.horizontal_margin)
            val marginItemDecoration = MarginItemDecoration(verticalMargin, horizontalMargin)

            addItemDecoration(marginItemDecoration)
            setHasFixedSize(true)

            this.layoutManager = layoutManager
            this.adapter = listUserAdapter

            listUserAdapter.setOnItemClickListener(this@MainActivity)
        }

        with(mainViewModel) {
            listUserResponse.observe(this@MainActivity) {
                when (it.status) {
                    StatusResponse.SUCCESS -> {
                        binding.rvListUsers.visibility = View.VISIBLE
                        binding.layoutError.root.visibility = View.INVISIBLE
                    }
                    StatusResponse.EMPTY -> showFailed(it.message!!)
                    StatusResponse.ERROR -> showFailed(it.message!!)
                }

                listUserAdapter.setData(it.body)
            }

            isLoading.observe(this@MainActivity) { showLoading(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        this@MainActivity.query = it
                        mainViewModel.getListUser(this@MainActivity.query)
                    }
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        return true
    }

    private fun showFailed(message: String) {
        binding.rvListUsers.visibility = View.INVISIBLE

        with(binding.layoutError) {
            root.visibility = View.VISIBLE
            tvErrorMessage.text = message
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading && binding.layoutError.root.isVisible) {
            binding.layoutError.root.visibility = View.INVISIBLE
        }

        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onUserItemClick(user: User) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
        startActivity(intent)
    }
}