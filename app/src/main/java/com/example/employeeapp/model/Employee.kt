package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName


data class Employee(
    @SerializedName(value = "id")
    val id: Int,

    @SerializedName(value = "name")
    val name: String,

    @SerializedName(value = "salary")
    val salary: Int,

    @SerializedName(value = "age")
    val age: Int
)