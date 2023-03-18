package com.robby.githubuser.api

import com.robby.githubuser.api.model.ListUserResponse
import com.robby.githubuser.api.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("search/users")
    fun getListUser(
        @Query("q") query: String
    ): Call<ListUserResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("") username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<User>>
}