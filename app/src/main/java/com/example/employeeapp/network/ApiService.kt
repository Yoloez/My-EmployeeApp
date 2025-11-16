package com.example.employeeapp.network

import com.example.employeeapp.model.CreateEmployeeRequest
import com.example.employeeapp.model.DeleteEmployeeResponse
import com.example.employeeapp.model.DetailEmployeeResponse
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.model.UpdateEmployeeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    //handle endpoint dari base url/employees
    @GET("employees")
    fun getAllEmployees(): Call<EmployeeResponse>

    @GET("employee/{id}")
    fun getEmployeeDetail(
        @Path("id") id: Int
    ): Call<DetailEmployeeResponse>

    @POST("create")
    fun createEmployee(
        @Body body: CreateEmployeeRequest
    ): Call<DetailEmployeeResponse>

    @PATCH("update/{id}")
    fun updateEmployee(
        @Path("id") id: Int,
        @Body body: UpdateEmployeeRequest
    ): Call<DetailEmployeeResponse>

    @DELETE("delete/{id}")
    fun deleteEmployee(
        @Path("id") id: Int
    ): Call<DeleteEmployeeResponse>
}