package com.robby.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robby.githubuser.api.ApiConfig
import com.robby.githubuser.api.ApiResponse
import com.robby.githubuser.api.model.ListUserResponse
import com.robby.githubuser.api.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUserResponse = MutableLiveData<ApiResponse<List<User>>>()
    val listUserResponse: LiveData<ApiResponse<List<User>>> = _listUserResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getListUser()
    }

    fun getListUser(query: String = "robby") {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListUser(query)
        client.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _listUserResponse.value = ApiResponse.success(it.items)
                    } ?: run {
                        _listUserResponse.value = ApiResponse.empty("Empty data!", emptyList())
                    }
                } else {
                    _listUserResponse.value = ApiResponse.error(response.message(), emptyList())
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                _isLoading.value = false
                _listUserResponse.value =
                    ApiResponse.error(t.message ?: "Failed to load data!", emptyList())
            }
        })
    }
}