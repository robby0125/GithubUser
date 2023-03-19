package com.robby.githubuser.api.model

import com.google.gson.annotations.SerializedName

data class ListUserResponse(
    @field:SerializedName("items")
    val items: List<User>
)