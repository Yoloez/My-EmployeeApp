package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class UpdateEmployeeRequest(
    @SerializedName(value = "name")
    val employeeName: String,

    @SerializedName(value = "salary")
    val salary: Int,

    @SerializedName(value = "age")
    val age: Int
)
