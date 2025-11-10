package com.example.employeeapp
import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityMainBinding
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val client = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {}
        loadEmployees()
    }
    fun loadEmployees() {
        val response = client.getAllEmployees()



        response.enqueue(object : Callback<EmployeeResponse> {
            override fun onResponse (
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ){
                if (!response.isSuccessful){
                    Toast.makeText(
                        this@MainActivity, "HTTP ${response.code()}", Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val body = response.body()
                val employees = body?.data.orEmpty()
                if (employees.isEmpty()){
                    Toast.makeText(
                        this@MainActivity,
                        "Data kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val names = employees.map {it.name}
                val listAdapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    names
                )
                binding.lvEmployee.adapter = listAdapter
                binding.lvEmployee.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
                    val id = employees[position].id
                    val intent = Intent(this@MainActivity, DetailEmployeeActivity::class.java)
                    intent.putExtra("EXTRA_ID", id)
                    startActivity(intent)}
            }

            override fun onFailure(call: Call<EmployeeResponse>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}