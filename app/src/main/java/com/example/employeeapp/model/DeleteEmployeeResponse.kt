package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class DeleteEmployeeResponse(
    @SerializedName(value = "status")
    val status: String,

    @SerializedName(value = "message")
    val message: String
)
