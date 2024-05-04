package com.test.planetapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResidentResponse(
    @SerializedName("name")
    val name: String?,

    @SerializedName("height")
    val height: String?,

    @SerializedName("gender")
    val gender: String?

)