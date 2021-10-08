package com.amalip.cocktailapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@Entity
@JsonClass(generateAdapter = true)
class User (
    @PrimaryKey(autoGenerate = false)
    val idUser: Int = 0,
    @Json(name = "strName") val userName: String = "",
    @Json(name = "strMail") val userMail: String = "",
    @Json(name = "strUserSource") val userUrl: String = "",
    @Json(name = "strToken") val userToken: String? = ""
){

}