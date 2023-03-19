package com.robby.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.robby.githubuser.R
import com.robby.githubuser.api.ApiResponse
import com.robby.githubuser.api.model.User
import com.robby.githubuser.databinding.FragmentUserFollowBinding
import com.robby.githubuser.enums.StatusResponse
import com.robby.githubuser.ui.adapter.ListUserAdapter
import com.robby.githubuser.utils.MarginItemDecoration

class UserFollowFragment : Fragment(), ListUserAdapter.OnItemClickListener {
    private var _binding: FragmentUserFollowBinding? = null
    private val binding get() = _binding!!

    private val listUserAdapter = ListUserAdapter()
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutError.root.visibility = View.INVISIBLE

        with(binding.rvListUsers) {
            val layoutManager = LinearLayoutManager(requireActivity())
            val verticalMargin = resources.getDimensionPixelSize(R.dimen.vertical_margin)
            val horizontalMargin = resources.getDimensionPixelSize(R.dimen.horizontal_margin)
            val itemDecoration = MarginItemDecoration(verticalMargin, horizontalMargin)

            addItemDecoration(itemDecoration)
            setHasFixedSize(true)

            this.layoutManager = layoutManager
            this.adapter = listUserAdapter

            listUserAdapter.setOnItemClickListener(this@UserFollowFragment)
        }

        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)

        if (position != null && username != null) {
            with(detailViewModel) {
                getUserFollow(username, position)

                isLoading.observe(viewLifecycleOwner) { showLoading(it) }

                when (position) {
                    0 -> followers.observe(viewLifecycleOwner) { provideData(it) }
                    1 -> followings.observe(viewLifecycleOwner) { provideData(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun provideData(response: ApiResponse<List<User>>) {
        when (response.status) {
            StatusResponse.SUCCESS -> listUserAdapter.setData(response.body)
            StatusResponse.EMPTY -> showFailed(response.message!!)
            StatusResponse.ERROR -> showFailed(response.message!!)
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

    override fun onUserItemClick(user: User) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
        startActivity(intent)
    }
}