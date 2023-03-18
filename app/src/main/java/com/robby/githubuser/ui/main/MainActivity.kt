package com.robby.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.robby.githubuser.R
import com.robby.githubuser.api.model.User
import com.robby.githubuser.databinding.ActivityMainBinding
import com.robby.githubuser.enums.StatusResponse
import com.robby.githubuser.ui.adapter.ListUserAdapter

class MainActivity : AppCompatActivity(), ListUserAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()
    private val listUserAdapter = ListUserAdapter()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvListUsers.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            val dividerIterator =
                DividerItemDecoration(this@MainActivity, layoutManager.orientation)

            this.layoutManager = layoutManager
            this.addItemDecoration(dividerIterator)
            this.adapter = listUserAdapter

            listUserAdapter.setOnItemClickListener(this@MainActivity)
        }

        with(mainViewModel) {
            listUserResponse.observe(this@MainActivity) {
                Log.d(TAG, "Status Response : ${it.status} with message : ${it.message}")
                when (it.status) {
                    StatusResponse.SUCCESS -> listUserAdapter.setData(it.body)
                    StatusResponse.EMPTY -> showFailed(it.message!!)
                    StatusResponse.ERROR -> showFailed(it.message!!)
                }
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
                    query?.let { mainViewModel.getListUser(it) }
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
        // TODO : show failed layout message
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onUserItemClick(user: User) {
        Toast.makeText(this, "Clicked : ${user.login}", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this@MainActivity, DetailActivity::class.java)
//        intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
//        startActivity(intent)
    }
}