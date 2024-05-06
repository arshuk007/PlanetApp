package com.test.planetapp.model

import com.google.gson.annotations.SerializedName

data class ResidentResponse(
    @SerializedName("name")
    val name: String?,

    @SerializedName("height")
    val height: String?,

    @SerializedName("gender")
    val gender: String?

)