package com.example.employeeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityDetailEmployeeBinding
import com.example.employeeapp.databinding.DialogCreateEmployeeBinding
import com.example.employeeapp.model.DeleteEmployeeResponse
import com.example.employeeapp.model.DetailEmployeeResponse
import com.example.employeeapp.model.UpdateEmployeeRequest
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEmployeeBinding
    private val client = ApiClient.getInstance()
    private var employeeId: Int = -1
    private var employeeName: String = ""
    private var employeeAge: Int = 0
    private var employeeSalary: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        employeeId = intent.getIntExtra("EXTRA_ID", -1)
        if (employeeId == -1) {
            Toast.makeText(
                this,
                "ID tidak valid",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        getEmployeeDetail(employeeId)

        binding.btnUpdate.setOnClickListener {
            showUpdateDialog()
        }

        binding.btnDelete.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun getEmployeeDetail(id: Int) {
        val response = client.getEmployeeDetail(id)

        response.enqueue(object : Callback<DetailEmployeeResponse> {
            override fun onResponse(
                call: Call<DetailEmployeeResponse?>,
                response: Response<DetailEmployeeResponse?>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                val employee = body?.data

                employeeName = employee?.name.orEmpty()
                employeeAge = employee?.age ?: 0
                employeeSalary = employee?.salary ?: 0

                binding.txtName.text = "Name: $employeeName"
                binding.txtAge.text = "Age: $employeeAge"
                binding.txtSalary.text = "Salary: $employeeSalary"
            }

            override fun onFailure(
                call: Call<DetailEmployeeResponse?>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showUpdateDialog() {
        val dialogBinding = DialogCreateEmployeeBinding.inflate(layoutInflater)

        // Pre-fill dengan data saat ini
        dialogBinding.etName.setText(employeeName)
        dialogBinding.etSalary.setText(employeeSalary.toString())
        dialogBinding.etAge.setText(employeeAge.toString())

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Employee")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Update") { dialog, _ ->
            val name = dialogBinding.etName.text.toString().trim()
            val salary = dialogBinding.etSalary.text.toString().toIntOrNull()
            val age = dialogBinding.etAge.text.toString().toIntOrNull()

            if (name.isEmpty() || salary == null || age == null) {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Isi semua data",
                    Toast.LENGTH_SHORT
                ).show()
                return@setPositiveButton
            }

            updateEmployee(name, salary, age) {
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun updateEmployee(name: String, salary: Int, age: Int, onSuccess: () -> Unit) {
        val body = UpdateEmployeeRequest(name, salary, age)
        client.updateEmployee(employeeId, body).enqueue(object : Callback<DetailEmployeeResponse> {
            override fun onResponse(
                call: Call<DetailEmployeeResponse>,
                response: Response<DetailEmployeeResponse>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "Update gagal: HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Employee berhasil diupdate",
                    Toast.LENGTH_SHORT
                ).show()

                onSuccess()
                getEmployeeDetail(employeeId) // Refresh data
            }

            override fun onFailure(call: Call<DetailEmployeeResponse>, t: Throwable) {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Gagal: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Employee")
            .setMessage("Apakah Anda yakin ingin menghapus employee ini?")
            .setPositiveButton("Hapus") { _, _ ->
                deleteEmployee()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteEmployee() {
        client.deleteEmployee(employeeId).enqueue(object : Callback<DeleteEmployeeResponse> {
            override fun onResponse(
                call: Call<DeleteEmployeeResponse>,
                response: Response<DeleteEmployeeResponse>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "Delete gagal: HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Employee berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()

                finish() // Kembali ke MainActivity
            }

            override fun onFailure(call: Call<DeleteEmployeeResponse>, t: Throwable) {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Gagal: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}