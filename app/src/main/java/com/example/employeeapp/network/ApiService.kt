package com.example.employeeapp.network

import com.example.employeeapp.model.DetailEmployeeResponse
import com.example.employeeapp.model.EmployeeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    //handle endpoint dari base url/employees
    @GET("employees")
    fun getAllEmployees(): Call<EmployeeResponse>

    @GET("employee/{id}")
    fun getEmployeeDetail(
        @Path("id") id: Int
    ): Call<DetailEmployeeResponse>
}