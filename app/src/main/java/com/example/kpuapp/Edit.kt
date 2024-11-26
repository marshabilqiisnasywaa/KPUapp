package com.example.kpuapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kpuapp.database.Note
import com.example.kpuapp.database.NoteDao
import com.example.kpuapp.database.NoteRoomDatabase
import com.example.kpuapp.databinding.ActivityEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Edit : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize DAO
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db?.noteDao()!!

        // Get the note ID passed via intent
        noteId = intent.getIntExtra("note_id", 0)

        // Load the existing data
        loadVoterData()

        // Set up the save button click listener
        binding.simpanButton.setOnClickListener {
            updateData()
        }
    }

    private fun loadVoterData() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Collect data from Flow
            noteDao.getVoterById(noteId).collect { voter ->
                withContext(Dispatchers.Main) {
                    // Check if the data is not null and update UI
                    binding.namaPemilihInputEdit.setText(voter.namaVoter)
                    binding.nikInputEdit.setText(voter.nik)
                    if (voter.gender == "Laki-laki") {
                        binding.genderMaleEdit.isChecked = true
                    } else {
                        binding.genderFemaleEdit.isChecked = true
                    }
                    binding.alamatInputEdit.setText(voter.alamat)
                }
            }
        }
    }

    private fun updateData() {
        val selectedGender = when (binding.genderGroupEdit.checkedRadioButtonId) {
            binding.genderMaleEdit.id -> "Laki-laki"
            binding.genderFemaleEdit.id -> "Perempuan"
            else -> ""
        }

        val namaPemilih = binding.namaPemilihInputEdit.text.toString()
        val nik = binding.nikInputEdit.text.toString()
        val alamat = binding.alamatInputEdit.text.toString()

        if (validateInput(namaPemilih, nik, alamat)) {
            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(
                    Note(
                        id = noteId,
                        namaVoter = namaPemilih,
                        nik = nik,
                        gender = selectedGender,
                        alamat = alamat
                    )
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Edit, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank()
    }
}
