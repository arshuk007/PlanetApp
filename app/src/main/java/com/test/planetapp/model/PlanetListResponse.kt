package com.test.planetapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlanetListResponse(
    @SerializedName("count")
    val count: Int,

    @SerializedName("next")
    val next: String?,

    @SerializedName("previous")
    val previous: String?,

    @SerializedName("detail")
    val detail: String?,

    @SerializedName("results")
    val planets: List<Planet>?

)

@Entity
@Parcelize
data class Planet(

    @PrimaryKey
    @SerializedName("url")
    val url: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("rotation_period")
    val rotationPeriod: String?,

    @SerializedName("orbital_period")
    val orbitalPeriod: String?,

    @SerializedName("diameter")
    val diameter: String?,

    @SerializedName("climate")
    val climate: String?,

    @SerializedName("gravity")
    val gravity: String?,

    @SerializedName("terrain")
    val terrain: String?,

    @SerializedName("surface_water")
    val surfaceWater: String?,

    @SerializedName("population")
    val population: String?,

    @SerializedName("created")
    val created: String?,

    @SerializedName("edited")
    val edited: String?,

    @SerializedName("residents")
    val residents: List<String>?,

    @SerializedName("films")
    val films: List<String>?

): Parcelable