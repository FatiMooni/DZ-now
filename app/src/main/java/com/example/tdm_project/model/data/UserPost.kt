package com.example.tdm_project.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserPost (
    @SerializedName("userID")
    @Expose
    var userId : String

)