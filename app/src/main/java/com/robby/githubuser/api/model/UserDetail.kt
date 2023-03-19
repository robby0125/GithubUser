package com.robby.githubuser.api.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("bio")
    val bio: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String
)
