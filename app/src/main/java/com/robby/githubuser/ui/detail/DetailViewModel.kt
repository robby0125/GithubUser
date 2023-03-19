package com.robby.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robby.githubuser.api.ApiConfig
import com.robby.githubuser.api.ApiResponse
import com.robby.githubuser.api.model.User
import com.robby.githubuser.api.model.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detail = MutableLiveData<ApiResponse<UserDetail?>>()
    val detail: LiveData<ApiResponse<UserDetail?>> = _detail

    private val _followers = MutableLiveData<ApiResponse<List<User>>>()
    val followers: LiveData<ApiResponse<List<User>>> = _followers

    private val _followings = MutableLiveData<ApiResponse<List<User>>>()
    val followings: LiveData<ApiResponse<List<User>>> = _followings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val defaultErrorMessage = "Failed to load data!"

    fun getUserDetail(username: String) {
        if (_detail.value != null) return

        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _detail.value = ApiResponse.success(it)
                    } ?: run {
                        _detail.value = ApiResponse.empty("Empty data!", null)
                    }
                } else {
                    _detail.value = ApiResponse.error(response.message(), null)
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _isLoading.value = false
                _detail.value = ApiResponse.error(t.message ?: "Failed to load data!", null)
            }
        })
    }

    fun getUserFollow(username: String, position: Int) {
        val hasFetchFollower = _followers.value != null
        val hasFetchFollowing = _followings.value != null

        if (position == 0 && hasFetchFollower || position == 1 && hasFetchFollowing) return

        _isLoading.value = true

        val client = if (position == 0) {
            ApiConfig.getApiService().getFollower(username)
        } else {
            ApiConfig.getApiService().getFollowing(username)
        }

        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false

                val apiResponse: ApiResponse<List<User>> = if (response.isSuccessful) {
                    val listUser = response.body()
                    if (listUser?.isNotEmpty() == true)
                        ApiResponse.success(listUser)
                    else
                        ApiResponse.empty("Nothing to show!", emptyList())
                } else {
                    val message = response.message().ifEmpty { defaultErrorMessage }
                    ApiResponse.error(message, emptyList())
                }

                if (position == 0)
                    _followers.value = apiResponse
                else
                    _followings.value = apiResponse
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false

                val apiResponse =
                    ApiResponse.error<List<User>>(t.message ?: defaultErrorMessage, emptyList())

                if (position == 0)
                    _followers.value = apiResponse
                else
                    _followings.value = apiResponse
            }
        })
    }
}