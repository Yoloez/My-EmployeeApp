package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName


data class Employee(
    @SerializedName(value = "id")
    val id: Int,

    @SerializedName(value = "employee_name")
    val name: String,

    @SerializedName(value = "employee_salary")
    val salary: Int,

    @SerializedName(value = "employee_age")
    val age: Int
)