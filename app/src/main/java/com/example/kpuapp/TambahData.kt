package com.example.kpuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpuapp.database.Note
import com.example.kpuapp.database.NoteDao
import com.example.kpuapp.database.NoteRoomDatabase
import com.example.kpuapp.databinding.ActivityTambahDataBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TambahData : AppCompatActivity() {
    private lateinit var binding: ActivityTambahDataBinding
    private lateinit var executorService: ExecutorService
    private lateinit var noteDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Executor Service for background operations
        executorService = Executors.newSingleThreadExecutor()

        // Initialize database and DAO
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db?.noteDao() ?: throw IllegalStateException("Database not initialized")

        // Set click listener for saving data
        binding.simpanButton.setOnClickListener {
            // Retrieve user inputs
            val namaPemilih = binding.namaPemilihInput.text.toString()
            val nik = binding.nikInput.text.toString()
            val alamat = binding.alamatInput.text.toString()

            // Get selected gender from radio buttons
            val selectedGender = when (binding.genderGroup.checkedRadioButtonId) {
                binding.genderMale.id -> "Laki-Laki"
                binding.genderFemale.id -> "Perempuan"
                else -> ""
            }

            // Validate input fields
            if (validateInput(namaPemilih, nik, alamat, selectedGender)) {
                // Insert data into the database
                insert(
                    Note(
                        namaVoter = namaPemilih,
                        nik = nik,
                        gender = selectedGender,
                        alamat = alamat
                    )
                )

                // Navigate back to MainActivity
                startActivity(Intent(this@TambahData, MainActivity::class.java))
                finish()
            } else {
                // Show validation error
                Toast.makeText(this@TambahData, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
                resetFields()
            }
        }
    }

    // Function to validate input fields
    private fun validateInput(
        namaPemilih: String,
        nik: String,
        alamat: String,
        gender: String
    ): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank() && gender.isNotBlank()
    }

    // Function to insert data into the database
    private fun insert(note: Note) {
        executorService.execute {
            noteDao.insert(note)
        }
    }

    // Function to reset input fields if validation fails
    private fun resetFields() {
        binding.namaPemilihInput.setText("")
        binding.nikInput.setText("")
        binding.alamatInput.setText("")
        binding.genderGroup.clearCheck()
    }
}
