package com.example.employeeapp
import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityMainBinding
import com.example.employeeapp.model.CreateEmployeeRequest
import com.example.employeeapp.model.DetailEmployeeResponse
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.databinding.DialogCreateEmployeeBinding
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
        with(binding) {
            btnCreate.setOnClickListener {
                showCreateDialog()
            }
        }
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

    private fun showCreateDialog(){
        val dialogBinding = DialogCreateEmployeeBinding.inflate(layoutInflater)

        var builder = AlertDialog.Builder(this)
        builder.setTitle("Create Employee")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Create") { dialog,   _ ->
            val name = dialogBinding.etName.text.toString().trim()
            val salary = dialogBinding.etSalary.text.toString().toIntOrNull()
            val age = dialogBinding.etAge.text.toString().toIntOrNull()

            if (name.isEmpty() || salary == null || age == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Isi semua data",
                    Toast.LENGTH_SHORT
                ).show()

                return@setPositiveButton
            }

            createEmployee(name, salary!!, age!!) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Cancel", null)

        builder.show()
    }
    private fun createEmployee(name: String, salary: Int, age: Int, onSuccess: () -> Unit) {
        val body = CreateEmployeeRequest(name, salary, age)
        client.createEmployee(body).enqueue(object : Callback<DetailEmployeeResponse> {
            override fun onResponse(c: Call<DetailEmployeeResponse>, r: Response<DetailEmployeeResponse>) {
                if (!r.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Create gagal: HTTP ${r.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }
                val emp = r.body()?.data ?: return
                onSuccess()
                loadEmployees() // refresh list
            }
            override fun onFailure(c: Call<DetailEmployeeResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}