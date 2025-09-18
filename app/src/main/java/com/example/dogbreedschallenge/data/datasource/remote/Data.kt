package com.example.dogbreedschallenge.data.datasource.remote

import com.google.gson.annotations.SerializedName

data class Data<T>(@SerializedName("message") val data: T,
                   @SerializedName("status") val status: String)