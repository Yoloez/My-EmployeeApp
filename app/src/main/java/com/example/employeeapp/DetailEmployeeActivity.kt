package com.example.employeeapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeeapp.databinding.ActivityDetailEmployeeBinding
import com.example.employeeapp.model.DetailEmployeeResponse
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEmployeeBinding
    val client = ApiClient.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

        }

        val employeeId = intent.getIntExtra("EXTRA_ID", -1)
        if(employeeId == -1){
            Toast.makeText(
                this,
                "ID tidak valid",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        getEmployeeDetail(employeeId)

    }

    fun getEmployeeDetail(id: Int) {
        val response = client.getEmployeeDetail(id)

        response.enqueue(object : Callback<DetailEmployeeResponse> {
            override fun onResponse(
                p0: Call<DetailEmployeeResponse?>,
                response: Response<DetailEmployeeResponse?>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val body = response.body()
                val employee = body?.data

                binding.txtName.text = employee?.name.toString()
                binding.txtAge.text = employee?.age.toString()
                binding.txtSalary.text = employee?.salary.toString()
            }

            override fun onFailure(
                p0: Call<DetailEmployeeResponse?>,
                p1: Throwable
            ) {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}